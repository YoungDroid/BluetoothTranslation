package com.oom.translatecommunication.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseActivity;
import com.oom.translatecommunication.model.BluetoothMsg;
import com.oom.translatecommunication.model.BluetoothMsg.ServerOrClient;
import com.oom.translatecommunication.network.CcBluetoothClientThread;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_client)
public class ActivityClient extends CcBaseActivity {

    @ViewById(R.id.lv_activity_client_content)
    ListView lvContent;
    @ViewById(R.id.et_client_send)
    EditText etSend;

    private ArrayAdapter< String > mAdapter;
    private List< String > msgList = new ArrayList< String >();

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
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothMsg.serviceOrClient = ServerOrClient.CLIENT;
        if ( BluetoothMsg.isOpen ) {
            String msgString = "连接已经打开,可以通信.如果要再建立连接,请先断开!";
            Message msg = LinkDetectedHandler.obtainMessage();
            msg.obj = msgString;
            LinkDetectedHandler.sendMessage( msg );
            Toast.makeText( this, msgString, Toast.LENGTH_SHORT ).show();
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
                String msgString = "address is null !";
                Message msg = LinkDetectedHandler.obtainMessage();
                msg.obj = msgString;
                LinkDetectedHandler.sendMessage( msg );
                Toast.makeText( this, msgString, Toast.LENGTH_SHORT ).show();
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
        if ( BluetoothMsg.serviceOrClient == ServerOrClient.CLIENT ) {
            clientConnectThread.close();
        }
        BluetoothMsg.isOpen = false;
        BluetoothMsg.serviceOrClient = ServerOrClient.NONE;
    }

    public void sendMessage( View view ) {
        if ( !etSend.getText().toString().equals( "" ) ) {
            clientConnectThread.sendMessage( etSend.getText().toString() );
        }
    }
}
