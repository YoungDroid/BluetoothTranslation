package com.oom.translatecommunication.view.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseActivity;
import com.oom.translatecommunication.utils.StringUtils;

import org.androidannotations.annotations.EActivity;

import java.util.Random;

/**
 * Created by CcYang on 2016/4/28.
 */
@EActivity(R.layout.activity_main)
public class ActivityMain extends CcBaseActivity {

    @Override
    public String tag() {
        return "ActivityMain";
    }

    @Override
    public void initView() {
        mToolbar.setTitle( "蓝牙通讯转移" );
        mToolbar.setTitleTextColor( Color.WHITE );
        mToolbar.setBackgroundColor( Color.RED );
        mToolbar.setNavigationIcon( null );
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initOtherThing() {

        short[] ss1 = new short[ 2 ];
        ss1[ 0 ] = ( byte ) new Random().nextInt( 128 );
        ss1[ 1 ] = ( byte ) new Random().nextInt( 128 );
        Log.e( "CcYang init", ss1[ 0 ] + "\t" + ss1[ 1 ] );
        byte[] b2 = StringUtils.shortsToBytes( ss1 );
        short[] ss2 = StringUtils.bytesToShorts( b2 );

//        for ( int i = 0; i < 4; i++ ) {
//            Log.e( "CcYang", "\t" + b2[ i ] );
//        }
        for ( int i = 0; i < 2; i++ ) {
            Log.e( "CcYang exchange", ss1[ i ] + "\t" + i + "\t" + ss2[ i ] );
        }
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    public void client( View view ) {
        ActivitySearchServer_.intent( this ).start();
    }

    public void service( View view ) {
        ActivityService_.intent( this ).start();
    }
}
