package com.oom.translatecommunication.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.oom.translatecommunication.core.CcAudioClient;
import com.oom.translatecommunication.core.CcAudioServer;
import com.oom.translatecommunication.model.TranslationMessage;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by CcYang on 2016/4/28.
 */
public class CcBluetoothServerThread extends Thread {
    /* 一些常量，代表服务器的名称 */
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";

    private Handler linkDetectedHandler = null;
    private BluetoothServerSocket mServerSocket = null;
    private BluetoothSocket socket = null;
    private CcBluetoothReadThread readThread = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private CcBluetoothController controller = null;
    private CcAudioClient audioClient;
    private CcAudioServer audioServer;

    public CcBluetoothServerThread( BluetoothAdapter mBluetoothAdapter, Handler handler ) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.linkDetectedHandler = handler;
    }

    //开启服务器
    @Override
    public void run() {
        try {
            /* 创建一个蓝牙服务器
             * 参数分别：服务器名称、UUID   */
            mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord( PROTOCOL_SCHEME_RFCOMM, UUID.fromString( "00001101-0000-1000-8000-00805F9B34FB" ) );

            Message msgLinking = linkDetectedHandler.obtainMessage();
            msgLinking.obj = new TranslationMessage( "请稍候，正在等待客户端的连接..." );
            linkDetectedHandler.sendMessage( msgLinking );
            /* 接受客户端的连接请求 */
            socket = mServerSocket.accept();

            Message msgLinked = linkDetectedHandler.obtainMessage();
            msgLinked.obj = new TranslationMessage( "客户端已经连接上！可以发送信息。" );
            linkDetectedHandler.sendMessage( msgLinked );
            //启动接受短信数据
            readThread = new CcBluetoothReadThread( socket, linkDetectedHandler );
            readThread.start();
            controller = new CcBluetoothController( mServerSocket, readThread, this );

//            //启动接受语音数据
//            audioClient = new CcAudioClient( socket, linkDetectedHandler );
//            audioClient.init();
//            audioClient.start();
//            audioServer = new CcAudioServer( socket, linkDetectedHandler );
//            audioServer.init();
//            audioServer.start();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public void close() {
        if ( controller != null ) {
            controller.shutdownServer();
        }
    }
}
