package com.oom.translatecommunication.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jude.utils.JUtils;
import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseActivity;
import com.oom.translatecommunication.model.BluetoothMsg;
import com.oom.translatecommunication.model.BluetoothMsg.ServerOrClient;
import com.oom.translatecommunication.network.CcBluetoothServerThread;

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

    @ViewById(R.id.lv_activity_service_content)
    ListView lvContent;

    private ArrayAdapter< String > mAdapter;
    private List< String > msgList = new ArrayList< String >();

    private CcBluetoothServerThread startServerThread = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    @Override
    public String tag() {
        return "ActivityService";
    }

    @Override
    public void initView() {
        mToolbar.setTitle( "主动端" );
        mToolbar.setBackgroundColor( Color.RED );

        mAdapter = new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, msgList );
        lvContent.setAdapter( mAdapter );
        lvContent.setFastScrollEnabled( true );
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initOtherThing() {

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
            String msgString = "连接已经打开,可以通信.如果要再建立连接,请先断开!";
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.obj = msgString;
            LinkDetectedHandler.sendMessage( msg );
            Toast.makeText( this, msgString, Toast.LENGTH_SHORT ).show();
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
            //Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_SHORT).show();
            Log.e( getClass().getSimpleName(), "result: " + msg.obj );
            msgList.add( ( String ) msg.obj );
            mAdapter.notifyDataSetChanged();
            lvContent.setSelection( msgList.size() - 1 );
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
}
