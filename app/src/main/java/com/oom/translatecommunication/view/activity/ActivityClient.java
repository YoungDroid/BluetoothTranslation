package com.oom.translatecommunication.view.activity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseActivity;
import com.oom.translatecommunication.model.BluetoothMsg;
import com.oom.translatecommunication.model.BluetoothMsg.ServerOrClient;
import com.oom.translatecommunication.model.TranslationMessage;
import com.oom.translatecommunication.network.CcBluetoothClientThread;
import com.oom.translatecommunication.utils.StringUtils;
import com.oom.translatecommunication.view.adapter.AdapterTranslation;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EActivity(R.layout.activity_client)
public class ActivityClient extends CcBaseActivity {

    @ViewById(R.id.rv_activity_client_content)
    RecyclerView rvContent;

    public static String translationReceiverSms = "TranslationReceiverSms";
    public static String smsReceiver1 = "candroid.intent.action.BOOT_COMPLETEN";
    public static String smsReceiver2 = "android.provider.Telephony.SMS_RECEIVED";

    public static String translationReceiverPhone = "TranslationReceiverPhone";
    private static String phoneReceiver1 = "candroid.intent.action.BOOT_COMPLETEN";
    private static String phoneReceiver2 = "android.intent.action.NEW_OUTGOING_CALL";
    private static String phoneReceiver3 = "android.intent.action.PHONE_STATE";

    private CcSmsReceiver smsReceiver;
    private CcPhoneReceiver phoneReceiver;

    private List< TranslationMessage > msgList;
    private AdapterTranslation adapterTranslation;
    private LinearLayoutManager layoutManager;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device = null;
    private CcBluetoothClientThread clientConnectThread = null;
    private String phoneNumber = null;

    @Override
    public String tag() {
        return "ActivityClient";
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver = new CcSmsReceiver();
        IntentFilter intentFilterSms = new IntentFilter( translationReceiverSms );
        intentFilterSms.addAction( smsReceiver1 );
        intentFilterSms.addAction( smsReceiver2 );
        registerReceiver( smsReceiver, intentFilterSms );

        phoneReceiver = new CcPhoneReceiver();
        IntentFilter intentFilterPhone = new IntentFilter( translationReceiverPhone );
        intentFilterPhone.addAction( phoneReceiver1 );
        intentFilterPhone.addAction( phoneReceiver2 );
        intentFilterPhone.addAction( phoneReceiver3 );
        registerReceiver( phoneReceiver, intentFilterPhone );
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver( smsReceiver );
        unregisterReceiver( phoneReceiver );
    }

    @Override
    public void initView() {
        mToolbar.setTitle( "被动端" );
        mToolbar.setBackgroundColor( Color.BLUE );
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initOtherThing() {
        msgList = new ArrayList<>();
        layoutManager = new LinearLayoutManager( this );
        layoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        rvContent.setHasFixedSize( true );
        rvContent.setLayoutManager( layoutManager );
        adapterTranslation = new AdapterTranslation( rvContent, msgList, R.layout.list_tranlsation );
        rvContent.setAdapter( adapterTranslation );
    }

    @Override
    public void refresh() {
        super.refresh();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothMsg.serviceOrClient = ServerOrClient.CLIENT;
        if ( BluetoothMsg.isOpen ) {
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.obj = new TranslationMessage( "连接已经打开,可以通信.如果要再建立连接,请先断开!" );
            LinkDetectedHandler.sendMessage( msg );
            return;
        }
        if ( BluetoothMsg.serviceOrClient == ServerOrClient.CLIENT ) {
            String address = BluetoothMsg.BlueToothAddress;
            if ( !address.equals( "null" ) ) {
                device = bluetoothAdapter.getRemoteDevice( address );
                clientConnectThread = new CcBluetoothClientThread( device, LinkDetectedHandler );
                clientConnectThread.start();
                BluetoothMsg.isOpen = true;
            } else {
                Message msg = LinkDetectedHandler.obtainMessage();
                msg.obj = new TranslationMessage( "Address is Null!" );
                LinkDetectedHandler.sendMessage( msg );
            }
        }
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            // exitActivity
            finish();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    private Handler LinkDetectedHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
            TranslationMessage translationMessage = ( TranslationMessage ) msg.obj;
            Log.e( getClass().getSimpleName(), "result: " + translationMessage.toString() );
            msgList.add( translationMessage );
            adapterTranslation.notifyDataSetChanged();
            if ( translationMessage.getType() != TranslationMessage.SystemInfo ) {
                clientConnectThread.sendMessage( translationMessage.toString() );
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( BluetoothMsg.serviceOrClient == ServerOrClient.CLIENT ) {
            clientConnectThread.close();
        }
        BluetoothMsg.isOpen = false;
        BluetoothMsg.serviceOrClient = ServerOrClient.NONE;
    }

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
                    System.out.println( "number:" + msg.getOriginatingAddress() + "   body:" + msg.getDisplayMessageBody() + "  time:" + msg.getTimestampMillis() );
                    TranslationMessage translationMessage = new TranslationMessage();
                    translationMessage.setNumber( msg.getOriginatingAddress() );
                    translationMessage.setContent( msg.getDisplayMessageBody() );
                    translationMessage.setType( TranslationMessage.MessageIn );
                    Message message = LinkDetectedHandler.obtainMessage();
                    message.obj = translationMessage;
                    LinkDetectedHandler.sendMessage( message );
                }
            }
        }
    }


    /**
     * Created by 小白杨 on 2016/3/4.
     */
    public class CcPhoneReceiver extends BroadcastReceiver {

        @Override
        public void onReceive( Context context, Intent intent ) {
            // 如果是拨打电话
            if ( intent.getAction().equals( Intent.ACTION_NEW_OUTGOING_CALL ) ) {
                phoneNumber = intent.getStringExtra( Intent.EXTRA_PHONE_NUMBER );
            } else {
                // 如果是来电
                TelephonyManager tManager = ( TelephonyManager ) context.getSystemService( Service.TELEPHONY_SERVICE );
                switch ( tManager.getCallState() ) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        phoneNumber = intent.getStringExtra( "incoming_number" );
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        TranslationMessage translationMessage = new TranslationMessage();
                        translationMessage.setNumber( phoneNumber );
                        translationMessage.setType( TranslationMessage.PhoneIn );
                        Message message = LinkDetectedHandler.obtainMessage();
                        message.obj = translationMessage;
                        LinkDetectedHandler.sendMessage( message );
                        break;
                }
            }
        }
    }


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
