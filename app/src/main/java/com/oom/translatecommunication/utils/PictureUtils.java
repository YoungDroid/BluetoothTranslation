package com.oom.translatecommunication.utils;

import android.graphics.Bitmap;

import com.oom.translatecommunication.config.StaticVariables;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Administrator on 2015/7/29.
 * Mail: xzlight@outlook.com
 */
public class PictureUtils {
    /**
     * Store image to SD card.
     */
    public static String storeImageToFile( String pictureName, Bitmap bitmap ) {
        if ( bitmap == null ) {
            return "Bitmap null.";
        }
        if ( pictureName == null || pictureName.equals( "" ) ) {
            return "Path error.";
        }

        String path = StaticVariables.sdCard_Path + "Image/" + pictureName;
        File file = new File( path );
        if ( !file.exists() ) {
            try {
                file.createNewFile();
            } catch ( IOException e ) {
                e.printStackTrace();
                return "Create File failed." + " Path: " + path;
            }
        }

        RandomAccessFile accessFile = null;
        ByteArrayOutputStream steam = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, steam );
        byte[] buffer = steam.toByteArray();

        try {
            accessFile = new RandomAccessFile( file, "rw" );
            accessFile.write( buffer );
        } catch ( Exception e ) {
            return "Output Error.";
        }

        try {
            steam.close();
            accessFile.close();
        } catch ( IOException e ) {
            //Note: do nothing.
            return "Close Error.";
        }

        return path;
    }
}
