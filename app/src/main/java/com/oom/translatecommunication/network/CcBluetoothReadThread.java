package com.oom.translatecommunication.network;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.oom.translatecommunication.model.TranslationMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by CcYang on 2016/4/28.
 */
public class CcBluetoothReadThread extends Thread {

    private Handler linkDetectedHandler = null;
    private BluetoothSocket socket = null;

    public CcBluetoothReadThread( BluetoothSocket socket, Handler handler ) {
        this.socket = socket;
        this.linkDetectedHandler = handler;
    }

    //读取数据
    @Override
    public void run() {
        byte[] buffer = new byte[ 1024 ];
        int bytes;
        InputStream mInStream = null;
        try {
            mInStream = socket.getInputStream();
        } catch ( IOException e1 ) {
            e1.printStackTrace();
        }
        while ( true ) {
            try {
                // Read from the InputStream
                if ( mInStream != null && ( bytes = mInStream.read( buffer ) ) > 0 ) {
                    byte[] buf_data = new byte[ bytes ];
                    for ( int i = 0; i < bytes; i++ ) {
                        buf_data[ i ] = buffer[ i ];
                    }
                    String stringRead = new String( buf_data );
                    Message message = linkDetectedHandler.obtainMessage();
                    TranslationMessage translationMessage = new TranslationMessage( stringRead, 0 );
                    Log.e( "CcYang", translationMessage.toString() );
                    message.obj = translationMessage;
                    linkDetectedHandler.sendMessage( message );
                }
            } catch ( IOException e ) {
                try {
                    mInStream.close();
                } catch ( IOException e1 ) {
                    e1.printStackTrace();
                }
                break;
            }
        }
    }
}
