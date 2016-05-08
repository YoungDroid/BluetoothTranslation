package com.oom.translatecommunication.config;

import android.media.AudioRecord;
import android.util.Log;

/**
 * Created by Administrator on 2016/5/3.
 */
public class SomeHelpWebsite {
    /*

    http://blog.csdn.net/zzqhost/article/details/7711935
    http://blog.csdn.net/yf210yf/article/details/6954933
    http://bbs.csdn.net/topics/370160555
    http://blog.163.com/fenglang_2006/blog/static/133662318200910300590726/
    http://blog.csdn.net/baimy1985/article/details/9275559
    http://blog.csdn.net/gf771115/article/details/38236335
    http://www.jianshu.com/p/3de46c75f8ce
    https://github.com/hesiyi/MyBluetoothChat



     */

    /*int frame_size = 320;//g726_32 : 4:1的压缩比
    byte[] audioData = new byte[ frame_size / 4 ];
    short[] encodeData = new short[ frame_size / 2 ];
    int num = 0;
    //库函数
    g726Codec codec = new g726Codec();
    short[] putIn = new short[ 160 ];
// int result= 0;
    while(running)
    {
        num = record.read( encodeData, 0, 160 );
        Log.e( TAG, "num:" + num );
        calc1( encodeData, 0, 160 );
        int wirteNum = track.write( encodeData, 0, num );
        if ( wirteNum == 160 ) {
            track.play();
        }
        System.arraycopy( encodeData, 0, putIn, 0, 80 );

        num = record.read( encodeData, 0, 160 );
        System.arraycopy( encodeData, 0, putIn, 80, 80 );
        num = record.read( putIn, 0, 160 );
        Log.e( TAG, "num:" + num );
        if ( num == AudioRecord.ERROR_INVALID_OPERATION || num == AudioRecord.ERROR_BAD_VALUE ) {
            Log.e( TAG, "Bad " );
            continue;
        }

        int iRet = codec.encode( encodeData, audioData );//先用G726进行编码
        Log.e( TAG, "encode iRet:" + iRet );

        iRet = codec.decode( audioData, encodeData );//然后用g726进行解码
        Log.e( TAG, "decode iRet:" + iRet );

        track.write( encodeData, 0, 160 );
        try {
            Thread.sleep( 200 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    void calc1( short[] lin, int off, int len ) {
        int i, j;
        for ( i = 0; i < len; i++ ) {
            j = lin[ i + off ];
            lin[ i + off ] = ( short ) ( j >> 2 );
        }
    }*/

    /*


				"int i, j, min, x = 0, y;",1
				"for (i = 0; i != points; i++)  {",2
				"    result[i] = INF;",3
				"}",4
				"result[0] = 0;",5
				"parents[0] = 0;",6
				"for (j = 0; j != points; j++){",7
				"    min = INF;",8
				"    for (i = 0; i != points; i++) {",9
				"        if (done[i] == false && result[i] < min) {",10
				"            min = result[i];",1
				"            x = i;",12
				"        }",13
				"    }",14
				"    done[x] = true;",15
				"    for (y = 0; y != points; y++) {",16
				"        if (source[x][y] != 0 ) {",17
				"    	     if (result[y] > result[x] + source[x][y]) {",18
				"                result[y] = result[x] + source[x][y];",19
				"                parents[y] = x",20
				"            }",21
				"        }",22
				"    }",23
				"}"};24

     */
}

