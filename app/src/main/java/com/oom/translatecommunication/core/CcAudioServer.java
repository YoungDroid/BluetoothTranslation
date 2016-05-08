package com.oom.translatecommunication.core;

import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;

import com.oom.translatecommunication.utils.StringUtils;

import java.io.DataInputStream;
import java.io.IOException;

public class CcAudioServer extends Thread {

    private AudioTrack audioTrackOut;
    private int outBufferSize;
    private byte[] outBytes;
    private short[] encodeData;
    private boolean keepRunning;
    private BluetoothSocket socket;
    private DataInputStream dataInputStream;
    private Handler linkDetectedHandler = null;

    public CcAudioServer( BluetoothSocket socket, Handler handler ) {
        this.socket = socket;
        this.linkDetectedHandler = handler;
    }

    public void init() {
        try {
            dataInputStream = new DataInputStream( socket.getInputStream() );
            keepRunning = true;
            outBufferSize = 160 * 4;
            audioTrackOut = new AudioTrack( AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, outBufferSize, AudioTrack.MODE_STREAM );
            outBytes = new byte[ outBufferSize ];
            encodeData = new short[ outBufferSize ];
        } catch ( Exception e ) {
            e.printStackTrace();
            responseMessage( "CcAudioServer init Exception." );
        }
    }

    private void responseMessage( String message ) {
        Message msg = linkDetectedHandler.obtainMessage();
        msg.what = 0;
        msg.obj = message;
        linkDetectedHandler.sendMessage( msg );
    }

    @Override
    public void run() {
        responseMessage( "CcAudioServer 开始接收数据." );
        audioTrackOut.play();
        while ( keepRunning ) {
            try {
                dataInputStream.read( outBytes );
                encodeData = StringUtils.bytesToShorts( outBytes );
                responseMessage( "CcAudioServer 转换降噪接收数据");
                audioTrackOut.write( encodeData, 0, encodeData.length );
            } catch ( Exception e ) {
                e.printStackTrace();
                responseMessage( "CcAudioServer running Exception." );
                break;
            }
        }
        audioTrackOut.stop();
        audioTrackOut = null;
        try {
            dataInputStream.close();
        } catch ( IOException e ) {
            e.printStackTrace();
            responseMessage( "CcAudioServer run IOException." );
        }
    }

    public void free() {
        keepRunning = false;
        try {
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            responseMessage( "CcAudioServer sleep exceptions..." );
        }
    }
}
