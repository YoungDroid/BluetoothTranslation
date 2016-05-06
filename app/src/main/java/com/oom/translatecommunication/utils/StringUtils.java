package com.oom.translatecommunication.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/5/30.
 */
public class StringUtils {
    public static String getRandomString( int length ) {
        //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTXYZ0123456789";
        //生成字符串从此序列中取
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < length; i++ ) {
            int number = random.nextInt( base.length() );
            sb.append( base.charAt( number ) );
        }
        return sb.toString();
    }

    public static String ToDBC( String input ) {
        char[] c = input.toCharArray();
        for ( int i = 0; i < c.length; i++ ) {
            if ( c[ i ] == 12288 ) {
                c[ i ] = ( char ) 32;
                continue;
            }
            if ( c[ i ] > 65280 && c[ i ] < 65375 ) c[ i ] = ( char ) ( c[ i ] - 65248 );
        }
        return new String( c );
    }

    /**
     * 计算字符串的权值
     *
     * @return 返回权值
     */
    public static int calculateWeight( String str ) {
        int weight = 0;
        for ( int i = 0; i < str.length(); i++ ) {
            weight += str.charAt( i );
        }
        return weight;
    }
    
    public static boolean isMobileNumber( String mobiles ) {
        Pattern p = Pattern.compile( "^((1[3-9][0-9]))\\d{8}$" );
        Matcher m = p.matcher( mobiles );
        return m.matches();
    }

    public static String millisToString( long millis, boolean text ) {
        boolean negative = millis < 0;
        millis = Math.abs( millis );
        int mini_sec = ( int ) millis % 1000;
        millis /= 1000;
        int sec = ( int ) ( millis % 60 );
        millis /= 60;
        int min = ( int ) ( millis % 60 );
        millis /= 60;
        int hours = ( int ) millis;

        String time;
        DecimalFormat format = ( DecimalFormat ) NumberFormat.getInstance( Locale.US );
        format.applyPattern( "00" );

        DecimalFormat format2 = ( DecimalFormat ) NumberFormat.getInstance( Locale.US );
        format2.applyPattern( "0" );
        if ( text ) {
            if ( millis > 0 ) time = ( negative ? "-" : "" ) + hours + "h" + format.format( min ) + "min";
            else if ( min > 0 ) time = ( negative ? "-" : "" ) + min + "min";
            else time = ( negative ? "-" : "" ) + sec + "socket";
        } else {
            if ( millis > 0 )
                time = ( negative ? "-" : "" ) + hours + ":" + format.format( min ) + ":" + format.format( sec )/* + ":" + format2.format(mini_sec)*/;
            else time = ( negative ? "-" : "" ) + min + ":" + format.format( sec )/* + ":" + format2.format(mini_sec)*/;
        }
        return time;
    }

    public static String getRandomLengthRandomString( int length ) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        int randomLength = random.nextInt( length );
        for ( int i = 0; i < randomLength; ++i ) {
            int number = random.nextInt( 62 );//[0,62)
            sb.append( str.charAt( number ) );
        }
        return sb.toString();
    }

    public static int stringToNumber( String string ) {
        return string.equals( "" ) ? -1 : Integer.parseInt( string );
    }

    public static void copy( String content, Context context ) {
        // 得到剪贴板管理器
        ClipboardManager cmb = ( ClipboardManager ) context.getSystemService( Context.CLIPBOARD_SERVICE );
        ClipData clipData = ClipData.newPlainText( "text", content );
        cmb.setPrimaryClip( clipData );
    }

    /**
     * @param number
     * @return 两位的字节数组
     * @功能 短整型与字节的转换
     */
    public static byte[] shortToByte( short number ) {
        int temp = number;
        byte[] b = new byte[ 2 ];
        for ( int i = 0; i < b.length; i++ ) {
            b[ i ] = Integer.valueOf( temp & 0xff ).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }

    /**
     * @param b
     * @return 短整型
     * @功能 字节的转换与短整型
     */
    public static short byteToShort( byte[] b ) {
        short s = 0;
        short s0 = ( short ) ( b[ 0 ] & 0xff );// 最低位
        short s1 = ( short ) ( b[ 1 ] & 0xff );
        s1 <<= 8;
        s = ( short ) ( s0 | s1 );
        return s;
    }

    public static byte[] shortsToBtyes( short[] shorts ) {
        byte[] bytes = new byte[ shorts.length * 2 ];

        for ( int i = 0; i < shorts.length; i++ ) {
            bytes[ i ] = shortToByte( shorts[ i ] )[ 0 ];
            bytes[ i == 0 ? i + 1 : i * 2 ] = shortToByte( shorts[ i ] )[ 1 ];
        }

        return bytes;
    }

    public static short[] bytesToShorts( byte[] bytes ) {
        short[] shorts = new short[ bytes.length / 2 ];

        for ( int i = 0; i < Math.ceil( bytes.length / 2.0 ); i++ ) {
            byte[] temp = new byte[ 2 ];
            temp[ 0 ] = bytes[ i ];
            if ( ( i == 0 ? i + 1 : i * 2 ) <= bytes.length ) {
                temp[ 1 ] = bytes[ i == 0 ? i + 1 : i * 2 ];
            } else {
                temp[ 1 ] = 0;
            }
            shorts[ i ] = byteToShort( temp );
        }

        return shorts;
    }
}
