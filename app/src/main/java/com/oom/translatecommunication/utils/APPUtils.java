package com.oom.translatecommunication.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.telephony.TelephonyManager;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/30.
 */
public class APPUtils {

    public static void doStartApplicationWithPackageName( Context context, String packagename ) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo( packagename, 0 );
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }
        if ( packageinfo == null ) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent( Intent.ACTION_MAIN, null );
        resolveIntent.addCategory( Intent.CATEGORY_LAUNCHER );
        resolveIntent.setPackage( packageinfo.packageName );

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List< ResolveInfo > resolveInfoList = context.getPackageManager().queryIntentActivities( resolveIntent, 0 );

        ResolveInfo resolveinfo = resolveInfoList.iterator().next();
        if ( resolveinfo != null ) {
            // packageName = 参数packName
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName.mainActivityName]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent( Intent.ACTION_MAIN );
            intent.addCategory( Intent.CATEGORY_LAUNCHER );

            // 设置ComponentName参数1:packageName:MainActivity路径
            ComponentName cn = new ComponentName( packageName, className );

            intent.setComponent( cn );
            context.startActivity( intent );
        }
    }

    public static boolean isAppInstalled( Context context, String packageName ) {
        final PackageManager packageManager = context.getPackageManager();
        List< PackageInfo > pinfo = packageManager.getInstalledPackages( 0 );
        List< String > pName = new ArrayList< String >();
        if ( pinfo != null ) {
            for ( int i = 0; i < pinfo.size(); i++ ) {
                String pn = pinfo.get( i ).packageName;
                pName.add( pn );
            }
        }
        return pName.contains( packageName );
    }

    public static String getPhoneImeiID(Context context) {
        String imeiString = "";
        TelephonyManager telephonyManager;
        telephonyManager = ( TelephonyManager ) context.getSystemService( Context.TELEPHONY_SERVICE );
        imeiString = telephonyManager.getDeviceId();
        return imeiString;
    }
    public static String getPhoneImsiID(Context context) {
        String imsiString = "";
        TelephonyManager telephonyManager;
        telephonyManager = ( TelephonyManager ) context.getSystemService( Context.TELEPHONY_SERVICE );
        imsiString = telephonyManager.getSubscriberId();
        return imsiString;
    }
}
