package com.oom.translatecommunication.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.oom.translatecommunication.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 小白杨 on 2016/3/4.
 */
public class CcSmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive( Context context, Intent intent ) {
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if ( null != bundle ) {
            Object[] smsObj = ( Object[] ) bundle.get( "pdus" );
            for ( Object object : smsObj ) {
                msg = SmsMessage.createFromPdu( ( byte[] ) object );
                Date date = new Date( msg.getTimestampMillis() );//时间
                SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                String receiveTime = format.format( date );
                System.out.println( "number:" + msg.getOriginatingAddress() + "   body:" + msg.getDisplayMessageBody() + "  time:" + msg.getTimestampMillis() );
                //在这里写自己的逻辑

                if ( StringUtils.isMobileNumber( msg.getOriginatingAddress() ) ||
                        StringUtils.isMobileNumber( msg.getOriginatingAddress().replace( "+86", "" ) ) ) {

                    String message = "来信号码:" + msg.getOriginatingAddress() + "\n来信时间:" + receiveTime + "\n短信内容:" + msg.getDisplayMessageBody();
                    sendSMS( "18983841884", message );
                }
//                Intent intentMain = new Intent( context, ActivityClient.class );
//                intentMain.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
//                intentMain.putExtra( "number", msg.getOriginatingAddress() );
//                intentMain.putExtra( "content", msg.getDisplayMessageBody());
//                intentMain.putExtra( "time", receiveTime );
//                context.startActivity( intentMain );
            }
        }
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    public void sendSMS( String phoneNumber, String message ) {
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        if ( message.length() > 70 ) {
            //拆分短信内容（手机短信长度限制）
            List< String > divideContents = smsManager.divideMessage( message );
            for ( String text : divideContents ) {
                smsManager.sendTextMessage( phoneNumber, null, text, null, null );
            }
        } else {
            smsManager.sendTextMessage( phoneNumber, null, message, null, null );
        }
    }
}
