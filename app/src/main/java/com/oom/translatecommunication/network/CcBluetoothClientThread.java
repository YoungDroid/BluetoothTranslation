package com.oom.translatecommunication.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.oom.translatecommunication.model.BluetoothMsg;

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
    private CcBluetoothController controller = null;

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
            Message msg2 = new Message();
            msg2.obj = "请稍候，正在连接服务器:" + BluetoothMsg.BlueToothAddress;
            msg2.what = 0;
            linkDetectedHandler.sendMessage( msg2 );

            socket.connect();

            Message msg = new Message();
            msg.obj = "已经连接上服务端！可以发送信息。";
            msg.what = 0;
            linkDetectedHandler.sendMessage( msg );
            //启动接受数据
            readThread = new CcBluetoothReadThread( socket, linkDetectedHandler );
            readThread.start();
            controller = new CcBluetoothController( this, readThread, socket );
        } catch ( IOException e ) {
            Log.e( "connect", "", e );
            Message msg = new Message();
            msg.obj = "连接服务端异常！断开连接重新试一试。";
            msg.what = 0;
            linkDetectedHandler.sendMessage( msg );
        }
    }

    public void close() {
        controller.shutdownClient();
    }
}
