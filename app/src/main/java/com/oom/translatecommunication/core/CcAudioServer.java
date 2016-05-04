package com.oom.translatecommunication.core;

import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;

import java.io.DataInputStream;
import java.io.IOException;

public class CcAudioServer extends Thread {

    protected AudioTrack audioTrackOut;
    protected int outBufferSize;
    protected byte[] outBytes;
    protected boolean keepRunning;
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
            outBufferSize = AudioTrack.getMinBufferSize( 8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT );
            audioTrackOut = new AudioTrack( AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, outBufferSize, AudioTrack.MODE_STREAM );
            outBytes = new byte[ outBufferSize ];
        } catch ( Exception e ) {
            e.printStackTrace();
            responseMessage( "Exception." );
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
        byte[] bytes_pkg = null;
        audioTrackOut.play();
        while ( keepRunning ) {
            try {
                dataInputStream.read( outBytes );
                bytes_pkg = outBytes.clone();
                audioTrackOut.write( bytes_pkg, 0, bytes_pkg.length );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        audioTrackOut.stop();
        audioTrackOut = null;
        try {
            dataInputStream.close();
        } catch ( IOException e ) {
            e.printStackTrace();
            responseMessage( "IOException." );
        }
    }

    public void free() {
        keepRunning = false;
        try {
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            responseMessage( "sleep exceptions..." );
        }
    }
}
