package com.oom.translatecommunication.view.activity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseActivity;
import com.oom.translatecommunication.app.CcBaseRecyclerAdapter.OnItemClickListener;
import com.oom.translatecommunication.model.BluetoothMsg;
import com.oom.translatecommunication.model.BluetoothMsg.ServerOrClient;
import com.oom.translatecommunication.model.TranslationMessage;
import com.oom.translatecommunication.network.CcBluetoothServerThread;
import com.oom.translatecommunication.view.adapter.AdapterTranslation;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CcYang on 2016/4/28.
 */
@EActivity(R.layout.activity_service)
public class ActivityService extends CcBaseActivity {

    public static final int BLUETOOTH_OPEN = 101;
    public static final int BLUETOOTH_DISCOVERABLE = 102;
    public static final int BLUETOOTH_DISCOVERABLE_TIME = 300;

    @ViewById(R.id.rv_activity_service_content)
    RecyclerView rvContent;

    private List< TranslationMessage > msgList;
    private AdapterTranslation adapterTranslation;
    private LinearLayoutManager layoutManager;

    private CcBluetoothServerThread startServerThread = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String phoneNumber = null;

    @Override
    public String tag() {
        return "ActivityService";
    }

    @Override
    public void initView() {
        mToolbar.setTitle( "主动端" );
        mToolbar.setBackgroundColor( Color.RED );
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
        adapterTranslation.setOnItemClickListener( new OnItemClickListener() {
            @Override
            public void onItemClick( View view, Object data, int position ) {
                if ( ((TranslationMessage) data).getType() != TranslationMessage.SystemInfo ) {
                    dialogAction.show();
                }
            }
        } );

        initActionDialog();
    }

    @Override
    public void refresh() {
        super.refresh();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if ( mBluetoothAdapter == null ) {
            JUtils.Toast( "请检查是否有蓝牙设备." );
        } else {
            openBluetooth();
        }

        startServer();
    }

    private void startServer() {
        BluetoothMsg.serviceOrClient = ServerOrClient.SERVICE;
        if ( BluetoothMsg.isOpen ) {
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.obj = new TranslationMessage( "连接已经打开,可以通信.如果要再建立连接,请先断开!" );
            LinkDetectedHandler.sendMessage( msg );
            return;
        }
        if ( BluetoothMsg.serviceOrClient == ServerOrClient.SERVICE ) {
            if ( startServerThread == null ) {
                startServerThread = new CcBluetoothServerThread( mBluetoothAdapter, LinkDetectedHandler );
            }
            startServerThread.start();
            BluetoothMsg.isOpen = true;
        }
    }

    private void openBluetooth() {
        // 打开蓝牙
        if ( mBluetoothAdapter.enable() ) {
            JUtils.Toast( "打开蓝牙成功." );
            discoverableBluetooth();
        } else {
            JUtils.Toast( "打开蓝牙失败." );
        }
    }

    private void discoverableBluetooth() {
        //打开本机的蓝牙发现功能（默认打开120秒，可以将时间最多延长至300秒）
        Intent discoveryIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE );
        discoveryIntent.putExtra( BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, BLUETOOTH_DISCOVERABLE_TIME );
        startActivityForResult( discoveryIntent, BLUETOOTH_DISCOVERABLE );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        switch ( requestCode ) {
            case BLUETOOTH_OPEN:
                break;
            case BLUETOOTH_DISCOVERABLE:
                switch ( resultCode ) {
                    case RESULT_OK:
                        JUtils.Toast( "蓝牙可见." );
                        break;
                    case RESULT_CANCELED:
                        JUtils.Toast( "蓝牙不可见." );
                        break;
                    case BLUETOOTH_DISCOVERABLE_TIME:
                        JUtils.Toast( "蓝牙" + resultCode + "s内可见." );
                        startServer();
                        break;
                }
                break;
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    private Handler LinkDetectedHandler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
            TranslationMessage translationMessage = ( TranslationMessage ) msg.obj;
            phoneNumber = translationMessage.getNumber();
            Log.e( getClass().getSimpleName(), "result: " + translationMessage.toString() );
            msgList.add( translationMessage );
            adapterTranslation.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if ( BluetoothMsg.serviceOrClient == ServerOrClient.SERVICE ) {
            startServerThread.close();
        }
        BluetoothMsg.isOpen = false;
        BluetoothMsg.serviceOrClient = ServerOrClient.NONE;
    }


    private Dialog dialogAction;

    private void initActionDialog() {
        dialogAction = new Dialog( this, R.style.Dialog );
        dialogAction.setContentView( R.layout.dialog_simple_dialog );
        //dialogShare.show();
        TextView textViewContent = ( TextView ) dialogAction.findViewById( R.id.tv_simple_dialog_content );
        textViewContent.setText( "选择操作" );
        Button btConfirm = ( Button ) dialogAction.findViewById( R.id.b_simple_dialog_confirm );
        btConfirm.setText( "直接回复" );
        btConfirm.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent intent = new Intent( Intent.ACTION_SENDTO, Uri.parse( "smsto:" + phoneNumber ) );
                intent.putExtra( "sms_body", "" );
                startActivity( intent );
            }
        } );
        Button btCancel = ( Button ) dialogAction.findViewById( R.id.b_simple_dialog_cancel );
        btCancel.setText( "转移回复" );
        btCancel.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View v ) {

            }
        } );
    }
}
