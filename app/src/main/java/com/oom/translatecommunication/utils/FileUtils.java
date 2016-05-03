package com.oom.translatecommunication.utils;

import android.graphics.Bitmap;
import android.util.Log;


import com.oom.translatecommunication.config.StaticVariables;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2015/10/19.
 */
public class FileUtils {
    public static void prepareDataFolder( String UserFolder ) {
        StaticVariables.USER_PATH = StaticVariables.ALBUM_PATH + UserFolder + File.separator;
        StaticVariables.CRASH_PATH = StaticVariables.USER_PATH + "Crash" + File.separator;

        File dirSecondFile = new File( StaticVariables.USER_PATH );
        if ( !dirSecondFile.exists() ) {
            if ( StaticVariables.DEBUG ) {
                Log.e( FileUtils.class.getSimpleName(), "Create User Folder \t" + StaticVariables.USER_PATH );
            }
            dirSecondFile.mkdirs();
        } else {
            if ( StaticVariables.DEBUG ) {
                Log.e( FileUtils.class.getSimpleName(), "User Folder exit path = " + StaticVariables.USER_PATH );
            }
        }
        dirSecondFile = new File( StaticVariables.CRASH_PATH );
        if ( !dirSecondFile.exists() ) {
            if ( StaticVariables.DEBUG ) {
                Log.e( FileUtils.class.getSimpleName(), "Create Crash Folder \t" + StaticVariables.CRASH_PATH );
            }
            dirSecondFile.mkdirs();
        } else {
            if ( StaticVariables.DEBUG ) {
                Log.e( FileUtils.class.getSimpleName(), "Crash Folder exit path = " + StaticVariables.CRASH_PATH );
            }
        }
    }

    public static File SavePicInLocal( Bitmap bitmap, String filePath ) {
        File file = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress( Bitmap.CompressFormat.PNG, 100, baos );
            byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
            file = new File( filePath );
            // 将字节数组写入到刚创建的图片文件中
            fos = new FileOutputStream( file );
            bos = new BufferedOutputStream( fos );
            bos.write( byteArray );

        } catch ( Exception e ) {
            e.printStackTrace();

        } finally {
            if ( baos != null ) {
                try {
                    baos.close();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
            if ( bos != null ) {
                try {
                    bos.close();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
            if ( fos != null ) {
                try {
                    fos.close();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile( File file ) {
        if ( file.exists() ) {
            if ( file.isFile() ) {
                file.delete();
            } else if ( file.isDirectory() ) {
                File files[] = file.listFiles();
                for ( int i = 0; i < files.length; i++ ) {
                    deleteFile( files[ i ] );
                }
            }
            file.delete();
        } else {
            if ( StaticVariables.DEBUG ) {
                Log.e( FileUtils.class.getSimpleName(), "delete file no exists " + file.getAbsolutePath() );
            }
        }
    }
}
