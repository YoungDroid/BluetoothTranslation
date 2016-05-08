package com.oom.translatecommunication.core;

import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.oom.translatecommunication.utils.StringUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class CcAudioClient extends Thread {
    private AudioTrack audioTrackOut;
    private AudioRecord audioRecordIn;
    private int inputBufferSize;
    private byte[] inputBytes;
    private short[] encodeData;
    private boolean keepRunning;
    private BluetoothSocket socket = null;
    private DataOutputStream dataOutputStream;
    private LinkedList< byte[] > linkedListBytes;
    private Handler linkDetectedHandler = null;

    public CcAudioClient( BluetoothSocket socket, Handler handler ) {
        this.socket = socket;
        this.linkDetectedHandler = handler;
    }

    public void init() {
        try {
            dataOutputStream = new DataOutputStream( socket.getOutputStream() );
//            CHANNEL_CONFIGURATION_MONO AudioRecord.getMinBufferSize( 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT )
            inputBufferSize = 160 * 4;
            audioRecordIn = new AudioRecord( MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, inputBufferSize );
            audioTrackOut = new AudioTrack( AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, inputBufferSize, AudioTrack.MODE_STREAM );
            inputBytes = new byte[ inputBufferSize ];
            encodeData = new short[ inputBufferSize ];
            keepRunning = true;
            linkedListBytes = new LinkedList< byte[] >();
        } catch ( IOException e ) {
            e.printStackTrace();
            responseMessage( "CcAudioClient init IOException." );
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
        try {
            byte[] bytes_pkg;
            audioRecordIn.startRecording();
            audioTrackOut.play();
            responseMessage( "CcAudioClient 开始录取数据." );
            while ( keepRunning ) {
                int number = audioRecordIn.read( encodeData, 0, inputBufferSize );
                deNoise( encodeData, 0, inputBufferSize );
                for ( int i = 0; i < encodeData.length; i++ ) {
                    Log.e( "CcYang", "" + encodeData[0] );
                }
                Log.e( "CcYang", "\n\t . "  );
                int writeNumber = audioTrackOut.write( encodeData, 0, number );
                if ( writeNumber == inputBufferSize ) {
                }
//                inputBytes = StringUtils.shortsToBytes( encodeData );
//                responseMessage( "CcAudioClient 转换降噪发出数据" );
//                bytes_pkg = inputBytes.clone();
//                if ( linkedListBytes.size() >= 2 ) {
//                    dataOutputStream.write( linkedListBytes.removeFirst(), 0, linkedListBytes.removeFirst().length );
//                }
//                linkedListBytes.add( bytes_pkg );
            }

            audioRecordIn.stop();
            audioRecordIn = null;
            inputBytes = null;
            dataOutputStream.close();
        } catch ( Exception e ) {
            e.printStackTrace();
            responseMessage( "CcAudioClient run Exception." );
        }
    }

    public void free() {
        keepRunning = false;
        try {
            Thread.sleep( 1000 );
        } catch ( Exception e ) {
            responseMessage( "CcAudioClient sleep exceptions..." );
        }
    }

    void deNoise( short[] lin, int off, int len ) {
        int i, j;
        for ( i = 0; i < len; i++ ) {
            j = lin[ i + off ];
            if ( Math.abs( j ) < 400 || Math.abs( j ) > 5000) {
                lin[ i + off ] = 0;
            }
        }
    }

//    void deNoise( short[] lin, int off, int len ) {
//        int i, j;
//        for ( i = 0; i < len; i++ ) {
//            j = lin[ i + off ];
//            lin[ i + off ] = ( short ) ( j >> 2 );
//        }
//    }
}
