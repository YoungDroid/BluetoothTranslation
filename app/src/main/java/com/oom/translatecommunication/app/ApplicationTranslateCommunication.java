package com.oom.translatecommunication.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;
import com.oom.translatecommunication.config.StaticVariables;
import com.oom.translatecommunication.utils.CrashHandler;
import com.oom.translatecommunication.utils.LocalDisplay;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

public class ApplicationTranslateCommunication extends Application {

    private static ApplicationTranslateCommunication instance;

    public ApplicationTranslateCommunication() {
    }

    // 单例模式获取唯一的MyApplication实例
    public static ApplicationTranslateCommunication getInstance() {
        if ( null == instance ) {
            instance = new ApplicationTranslateCommunication();
        }
        return instance;
    }

    public void exitApp() {
        JActivityManager.getInstance().closeAllActivity();
        System.exit( 0 );
    }

    // Change to Other Activity
    public void changeToOtherActivity( Activity activity, Class< ? > cls, boolean isFinish ) {
        Intent intent = new Intent( activity, cls );
        activity.startActivity( intent );
        if ( isFinish ) {
            JActivityManager.getInstance().closeActivity( activity );
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // JUtils
        JUtils.initialize( this );
        JUtils.setDebug( StaticVariables.DEBUG, StaticVariables.APP_NAME );

        // local display
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = ( WindowManager ) getSystemService( Context.WINDOW_SERVICE );
        wm.getDefaultDisplay().getMetrics( dm );
        LocalDisplay.init( dm );

        // ImageViewLoad...
        Fresco.initialize( this );

        // 异常文件
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init( this );
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}