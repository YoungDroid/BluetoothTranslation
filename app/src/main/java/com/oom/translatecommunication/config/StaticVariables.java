package com.oom.translatecommunication.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by 小白杨 on 2016/3/4.
 */
public class StaticVariables {
    public static boolean DEBUG = true;
    public static String APP_NAME = "通讯转移";

    // sdCard Path
    public static final String sdCard_Path = "/sdcard/Translate/";
    public static final String AppFolder = "Translate";
    public static final String ALBUM_PATH = Environment.getExternalStorageDirectory()
            + File.separator + StaticVariables.AppFolder + File.separator;
    public static String USER_PATH = ALBUM_PATH + "User" + File.separator;
    public static String CRASH_PATH = USER_PATH + "Crash" + File.separator;
}
