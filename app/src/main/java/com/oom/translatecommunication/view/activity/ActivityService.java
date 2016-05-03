package com.oom.translatecommunication.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
