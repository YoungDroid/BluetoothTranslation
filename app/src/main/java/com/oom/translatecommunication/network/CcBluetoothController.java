package com.oom.translatecommunication.network;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

/**
 * Created by CcYang on 2016/4/28.
 */
public class CcBluetoothController {

    private BluetoothServerSocket mServerSocket = null;
    private CcBluetoothServerThread startServerThread = null;
    private CcBluetoothClientThread clientConnectThread = null;
    private BluetoothSocket socket = null;
    private CcBluetoothReadThread readThread = null;

    public CcBluetoothController( CcBluetoothClientThread clientConnectThread, CcBluetoothReadThread readThread, BluetoothSocket socket ) {
        this.clientConnectThread = clientConnectThread;
        this.readThread = readThread;
        this.socket = socket;
    }

    public CcBluetoothController( BluetoothServerSocket mServerSocket, CcBluetoothReadThread readThread, CcBluetoothServerThread startServerThread ) {
        this.mServerSocket = mServerSocket;
        this.readThread = readThread;
        this.startServerThread = startServerThread;
    }

    /* 停止服务器 */
    public void shutdownServer() {
        new Thread() {
            @Override
            public void run() {
                if ( startServerThread != null ) {
                    startServerThread.interrupt();
                    startServerThread = null;
                }
                if ( readThread != null ) {
                    readThread.interrupt();
                    readThread = null;
                }
                try {
                    if ( socket != null ) {
                        socket.close();
                        socket = null;
                    }
                    if ( mServerSocket != null ) {
                        /* 关闭服务器 */
                        mServerSocket.close();
                        mServerSocket = null;
                    }
                } catch ( IOException e ) {
                    Log.e( getClass().getSimpleName(), "serverSocket.close()", e );
                }
            }
        }.start();
    }

    /* 停止客户端连接 */
    public void shutdownClient() {
        new Thread() {
            @Override
            public void run() {
                if ( clientConnectThread != null ) {
                    clientConnectThread.interrupt();
                    clientConnectThread = null;
                }
                if ( readThread != null ) {
                    readThread.interrupt();
                    readThread = null;
                }
                if ( socket != null ) {
                    try {
                        socket.close();
                    } catch ( IOException e ) {
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        }.start();
    }
}
