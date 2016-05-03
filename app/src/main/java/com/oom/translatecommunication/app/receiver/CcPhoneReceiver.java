package com.oom.translatecommunication.app.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 小白杨 on 2016/3/4.
 */
public class CcPhoneReceiver extends BroadcastReceiver {

    private static final String TAG = "message";
    private static boolean mIncomingFlag = false;
    private static String phoneNumber = null;

    @Override
    public void onReceive( Context context, Intent intent ) {
        // 如果是拨打电话
        if ( intent.getAction().equals( Intent.ACTION_NEW_OUTGOING_CALL ) ) {
            mIncomingFlag = false;
            String phoneNumber = intent.getStringExtra( Intent.EXTRA_PHONE_NUMBER );
            Log.i( TAG, "call OUT:" + phoneNumber );

        } else {
            // 如果是来电
            TelephonyManager tManager = ( TelephonyManager ) context.getSystemService( Service.TELEPHONY_SERVICE );
            switch ( tManager.getCallState() ) {

                case TelephonyManager.CALL_STATE_RINGING:

                    phoneNumber = intent.getStringExtra( "incoming_number" );
//                    Intent intentMain = new Intent( context, ActivityClient.class );
//                    intentMain.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
//                    intentMain.putExtra( "phoneNumber", phoneNumber );
//                    intentMain.putExtra( "time", receiveTime );
//                    context.startActivity( intentMain );
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if ( mIncomingFlag ) {
                        Log.i( TAG, "incoming ACCEPT :" + phoneNumber );
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Date date = new Date( System.currentTimeMillis() );//时间
                    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                    String receiveTime = format.format( date );
                    String message = "来电号码:" + phoneNumber + "\n来电时间:" + receiveTime;
                    sendSMS( "18983841884", message );

                    if ( mIncomingFlag ) {
                        Log.i( TAG, "incoming IDLE" );
                    }
                    break;
            }
        }
    }

    /**
     * 直接调用短信接口发短信
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber,String message){
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        if(message.length() > 70) {
            //拆分短信内容（手机短信长度限制）
            List<String> divideContents = smsManager.divideMessage(message);
            for (String text : divideContents) {
                smsManager.sendTextMessage(phoneNumber, null, text, null, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
    }
}