package com.oom.translatecommunication.network;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CcBluetoothSendThread {

    private Handler linkDetectedHandler = null;
    private BluetoothSocket socket = null;
    private OutputStream outputStream = null;

    public CcBluetoothSendThread( @NonNull BluetoothSocket socket, @NonNull Handler handler ) {
        this.socket = socket;
        this.linkDetectedHandler = handler;
        try {
            this.outputStream = socket.getOutputStream();
        } catch ( IOException e ) {
            e.printStackTrace();
            Message msg = linkDetectedHandler.obtainMessage();
            msg.obj = "初始化发送失败.";
            msg.what = 0;
            linkDetectedHandler.sendMessage( msg );
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.write( message.getBytes() );
            Message msg = linkDetectedHandler.obtainMessage();
            msg.obj = "发送成功：" + message;
            msg.what = 0;
            linkDetectedHandler.sendMessage( msg );
        } catch ( IOException e ) {
            e.printStackTrace();
            Message msg = linkDetectedHandler.obtainMessage();
            msg.obj = "发送失败.";
            msg.what = 0;
            linkDetectedHandler.sendMessage( msg );
        }
    }
}
