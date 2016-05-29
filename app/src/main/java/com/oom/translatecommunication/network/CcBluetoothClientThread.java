package com.oom.translatecommunication.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.oom.translatecommunication.core.CcAudioClient;
import com.oom.translatecommunication.core.CcAudioServer;
import com.oom.translatecommunication.model.BluetoothMsg;
import com.oom.translatecommunication.model.TranslationMessage;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by CcYang on 2016/4/28.
 */
public class CcBluetoothClientThread extends Thread {

    private Handler linkDetectedHandler = null;
    private BluetoothSocket socket = null;
    private BluetoothDevice device = null;
    private CcBluetoothReadThread readThread = null;
    private CcBluetoothSendThread sendThread = null;
    private CcBluetoothController controller = null;
    private CcAudioClient audioClient;
    private CcAudioServer audioServer;

    public CcBluetoothClientThread( BluetoothDevice device, Handler linkDetectedHandler ) {
        this.device = device;
        this.linkDetectedHandler = linkDetectedHandler;
    }

    //开启客户端
    @Override
    public void run() {
        try {
            //创建一个Socket连接：只需要服务器在注册时的UUID号
            socket = device.createRfcommSocketToServiceRecord( UUID.fromString( "00001101-0000-1000-8000-00805F9B34FB" ) );
            //连接
            Message msg2 = linkDetectedHandler.obtainMessage();
            msg2.obj = new TranslationMessage( "请稍候，正在连接服务器:" + BluetoothMsg.BlueToothAddress );
            linkDetectedHandler.sendMessage( msg2 );
            socket.connect();

            Message msg = linkDetectedHandler.obtainMessage();
            msg.obj = new TranslationMessage( "已经连接上服务端！可以发送信息。" );
            linkDetectedHandler.sendMessage( msg );
            //启动接受短信数据
            readThread = new CcBluetoothReadThread( socket, linkDetectedHandler );
            readThread.start();
            sendThread = new CcBluetoothSendThread( socket, linkDetectedHandler );
            controller = new CcBluetoothController( this, readThread, socket );

//            //启动语音获取
//            audioClient = new CcAudioClient( socket, linkDetectedHandler );
//            audioClient.init();
//            audioClient.start();
//            audioServer = new CcAudioServer( socket, linkDetectedHandler );
//            audioServer.init();
//            audioServer.start();
        } catch ( IOException e ) {
            Message msg = linkDetectedHandler.obtainMessage();
            msg.obj = new TranslationMessage( "连接服务端异常！断开连接重新试一试。" );
            linkDetectedHandler.sendMessage( msg );
        }
    }

    public void close() {
        if ( controller != null ) {
            controller.shutdownClient();
        }
    }

    public void sendMessage( String message ) {
        if ( sendThread != null ) {
            sendThread.sendMessage( message );
        }
    }
}
