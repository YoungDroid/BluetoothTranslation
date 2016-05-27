package com.oom.translatecommunication.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;

import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseActivity;
import com.oom.translatecommunication.model.BluetoothMsg;
import com.oom.translatecommunication.model.BluetoothMsg.ServerOrClient;
import com.oom.translatecommunication.model.TranslationMessage;
import com.oom.translatecommunication.network.CcBluetoothClientThread;
import com.oom.translatecommunication.view.adapter.AdapterTranslation;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_client)
public class ActivityClient extends CcBaseActivity {

    @ViewById(R.id.rv_activity_client_content)
    RecyclerView rvContent;

    private List< TranslationMessage > msgList;
    private AdapterTranslation adapterTranslation;
    private LinearLayoutManager layoutManager;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device = null;
    private CcBluetoothClientThread clientConnectThread = null;

    @Override
    public String tag() {
        return "ActivityClient";
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
            Log.e( getClass().getSimpleName(), "result: " + ( ( TranslationMessage ) msg.obj ).toString() );
            msgList.add( ( TranslationMessage ) msg.obj );
            adapterTranslation.notifyDataSetChanged();
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
}
