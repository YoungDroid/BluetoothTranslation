package com.oom.translatecommunication.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.jude.utils.JUtils;
import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseActivity;
import com.oom.translatecommunication.app.CcBaseRecyclerAdapter.OnItemClickListener;
import com.oom.translatecommunication.model.BluetoothMsg;
import com.oom.translatecommunication.view.adapter.AdapterTargetNumberBluetooth;
import com.oom.translatecommunication.widget.textview.CcMagicTextView;
import com.oom.translatecommunication.widget.togglebutton.ToggleButton;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 小白杨 on 2016/3/8.
 */
@EActivity(R.layout.activity_target_number)
public class ActivitySearchServer extends CcBaseActivity {

    public static final int BLUETOOTH_OPEN = 101;
    public static final int BLUETOOTH_DISCOVERABLE = 102;
    public static final int BLUETOOTH_DISCOVERABLE_TIME = 300;

    @ViewById(R.id.tv_target_number_state)
    TextView tvTargetNumberState;
    @ViewById(R.id.tb_target_number_bluetooth_state)
    ToggleButton tbTargetNumberBluetoothState;
    @ViewById(R.id.mtv_target_number_search_bluetooth)
    CcMagicTextView mtvTargetNumberSearchBluetooth;
    @ViewById(R.id.rv_target_number_bluetooth_list)
    RecyclerView rvTargetNumberBluetoothList;
    @ViewById(R.id.rv_target_search_list_connected)
    RecyclerView rvSearchServerBluetoothListConnected;

    private ActionBar actionBar;
    private BluetoothAdapter bluetoothAdapter;

    private LinearLayoutManager layoutManagerBluetooth;
    private AdapterTargetNumberBluetooth adapterBluetooth;
    private ArrayList< String > listBluetooth;

    private LinearLayoutManager layoutManagerBluetoothConnected;
    private AdapterTargetNumberBluetooth adapterBluetoothConnected;
    private ArrayList< String > listBluetoothConnected;

    private int searchCount = 0;

    private JSONArray saveConnected;

    @Override
    public String tag() {
        return "ActivitySearchServer";
    }

    @Override
    public void initView() {

        actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setHomeButtonEnabled( true );
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setTitle( "目标号" );
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if ( bluetoothAdapter == null ) {
            JUtils.Toast( "请检查是否有蓝牙设备." );
        } else {
            openBluetooth();
        }

        try {
            saveConnected = new JSONArray( sharedPreferences.getString( "saveConnect", "" ) );
        } catch ( JSONException e ) {
            saveConnected = new JSONArray();
        }

        layoutManagerBluetooth = new LinearLayoutManager( this );
        layoutManagerBluetooth.setOrientation( LinearLayoutManager.HORIZONTAL );
        rvTargetNumberBluetoothList.setLayoutManager( layoutManagerBluetooth );
        rvTargetNumberBluetoothList.setHasFixedSize( true );
        listBluetooth = new ArrayList<>();
        adapterBluetooth = new AdapterTargetNumberBluetooth( rvTargetNumberBluetoothList, listBluetooth, R.layout.list_target_number_bluetooth );
        rvTargetNumberBluetoothList.setItemAnimator( new DefaultItemAnimator() );
        rvTargetNumberBluetoothList.setAdapter( adapterBluetooth );
        adapterBluetooth.setOnItemClickListener( new OnItemClickListener() {
            @Override
            public void onItemClick( View view, Object data, int position ) {
                final String msg = listBluetooth.get( position );
                AlertDialog.Builder dialog = new AlertDialog.Builder( ActivitySearchServer.this );// 定义一个弹出框对象
                dialog.setTitle( "Confirmed connecting device" );
                dialog.setMessage( msg );
                dialog.setPositiveButton( "connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        BluetoothMsg.BlueToothAddress = msg.substring( msg.length() - 17 );
                        if ( BluetoothMsg.lastblueToothAddress != BluetoothMsg.BlueToothAddress ) {
                            BluetoothMsg.lastblueToothAddress = BluetoothMsg.BlueToothAddress;
                        }
                        try {
                            JSONObject connected = new JSONObject();
                            connected.put( "address", msg );
                            saveConnected.put( connected );
                            editor.putString( "saveConnect", saveConnected.toString() );
                            editor.apply();
                        } catch ( JSONException e ) {
                            e.printStackTrace();
                        }
                        ActivityClient_.intent( ActivitySearchServer.this ).start();
                    }
                } );
                dialog.setNegativeButton( "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        BluetoothMsg.BlueToothAddress = null;
                    }
                } );
                dialog.show();
            }
        } );

        layoutManagerBluetoothConnected = new LinearLayoutManager( this );
        layoutManagerBluetoothConnected.setOrientation( LinearLayoutManager.HORIZONTAL );
        rvSearchServerBluetoothListConnected.setLayoutManager( layoutManagerBluetoothConnected );
        rvSearchServerBluetoothListConnected.setHasFixedSize( true );
        listBluetoothConnected = new ArrayList<>();
        for ( int i = 0; i < saveConnected.length(); i++ ) {
            try {
                listBluetoothConnected.add( ((JSONObject) saveConnected.opt( i )).getString( "address" ) );
            } catch ( JSONException e ) {
                e.printStackTrace();
            }
        }
        if ( listBluetoothConnected.size() > 0 ) {
            if ( rvSearchServerBluetoothListConnected.getVisibility() == View.GONE ) {
                rvSearchServerBluetoothListConnected.setVisibility( View.VISIBLE );
            }
        }
        adapterBluetoothConnected = new AdapterTargetNumberBluetooth( rvSearchServerBluetoothListConnected, listBluetoothConnected, R.layout.list_target_number_bluetooth );
        rvSearchServerBluetoothListConnected.setItemAnimator( new DefaultItemAnimator() );
        rvSearchServerBluetoothListConnected.setAdapter( adapterBluetoothConnected );
        adapterBluetoothConnected.setOnItemClickListener( new OnItemClickListener() {
            @Override
            public void onItemClick( View view, Object data, int position ) {
                final String msg = listBluetoothConnected.get( position );
                AlertDialog.Builder dialog = new AlertDialog.Builder( ActivitySearchServer.this );// 定义一个弹出框对象
                dialog.setTitle( "Confirmed connecting device" );
                dialog.setMessage( msg );
                dialog.setPositiveButton( "connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        BluetoothMsg.BlueToothAddress = msg.substring( msg.length() - 17 );
                        if ( BluetoothMsg.lastblueToothAddress != BluetoothMsg.BlueToothAddress ) {
                            BluetoothMsg.lastblueToothAddress = BluetoothMsg.BlueToothAddress;
                        }
                        ActivityClient_.intent( ActivitySearchServer.this ).start();
                    }
                } );
                dialog.setNegativeButton( "cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialog, int which ) {
                        BluetoothMsg.BlueToothAddress = null;
                    }
                } );
                dialog.show();
            }
        } );
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            // exitActivity
            back( null );
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    public void bluetooth( View view ) {
        changeBluetoothState();
    }

    private void changeBluetoothState() {
        if ( !bluetoothAdapter.isEnabled() ) {
            openBluetooth();
        } else {
            closeBluetooth();
        }
        bluetoothStateChanged();
    }

    private void openBluetooth() {
        // 打开蓝牙
        if ( bluetoothAdapter.enable() ) {
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

    private void closeBluetooth() {
        // 关闭蓝牙
        if ( bluetoothAdapter.disable() ) {
            JUtils.Toast( "关闭蓝牙成功." );
        } else {
            JUtils.Toast( "关闭蓝牙失败." );
        }
    }

    private void bluetoothStateChanged() {
        if ( bluetoothAdapter.isEnabled() ) {
            tvTargetNumberState.setText( "蓝牙状态{打开}{搜索中...}" );
            tvTargetNumberState.setText( String.format( getString( R.string.target_number_bluetooth_state ), String.format( getString( R.string.target_number_bluetooth_state_on ), "打开", "搜索中...", searchCount ) ) );
            tbTargetNumberBluetoothState.setToggleOn();
            showSearchWords();
        } else {
            tvTargetNumberState.setText( String.format( getString( R.string.target_number_bluetooth_state ), "关闭" ) );
            tbTargetNumberBluetoothState.setToggleOff();
            hideSearchWords();
        }
    }

    private void showSearchWords() {
        mtvTargetNumberSearchBluetooth.setVisibility( View.VISIBLE );
    }

    private void hideSearchWords() {
        mtvTargetNumberSearchBluetooth.setVisibility( View.GONE );
        if ( listBluetooth != null ) {
            listBluetooth.clear();
            adapterBluetooth.notifyDataSetChanged();
        }
        rvTargetNumberBluetoothList.setVisibility( View.GONE );
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
                        bluetoothStateChanged();
                        // 注册BroadcastReceiver
                        IntentFilter filter = new IntentFilter( BluetoothDevice.ACTION_FOUND );
                        registerReceiver( mReceiver, filter ); // 不要忘了之后解除绑定
                        bluetoothAdapter.startDiscovery();
                        break;
                }
                break;
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

    // 创建一个接收ACTION_FOUND广播的BroadcastReceiver
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive( Context context, Intent intent ) {
            Logger.init().setLogLevel( LogLevel.FULL );
            Logger.t( "Receiver" ).d( "Action " + intent.getAction() );

            String action = intent.getAction();
            // 发现设备
            if ( BluetoothDevice.ACTION_FOUND.equals( action ) ) {
                // 从Intent中获取设备对象
                BluetoothDevice device = intent.getParcelableExtra( BluetoothDevice.EXTRA_DEVICE );
                // 将设备名称和地址放入array adapter，以便在ListView中显示
                listBluetooth.add( device.getName() + "\n" + device.getAddress() );
                searchCount++;
                tvTargetNumberState.setText( String.format( getString( R.string.target_number_bluetooth_state ), String.format( getString( R.string.target_number_bluetooth_state_on ), "打开", "搜索中...", searchCount ) ) );

                if ( rvTargetNumberBluetoothList.getVisibility() == View.GONE ) {
                    rvTargetNumberBluetoothList.setVisibility( View.VISIBLE );
                }
                adapterBluetooth.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver( mReceiver );
    }
}
