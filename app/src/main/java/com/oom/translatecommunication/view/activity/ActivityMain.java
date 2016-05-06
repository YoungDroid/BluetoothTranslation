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

        byte[] b1 = new byte[2];
        short s1 = ( short ) new Random().nextInt( 128 );
        b1[0] = ( byte ) new Random().nextInt( 128 );
        b1[1] = ( byte ) new Random().nextInt( 128 );

        byte[] b2 = StringUtils.shortsToBtyes( StringUtils.bytesToShorts( b1 ) );
        short s2 = StringUtils.byteToShort( StringUtils.shortToByte( s1 ) );

        Log.e( "CcYang", s1 + "\t" + b1[0] + "\t" + b1[1]);
        Log.e( "CcYang", s2 + "\t" + b2[0] + "\t" + b2[1]);
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
        ActivityTargetNumber_.intent( this ).start();
    }

    public void service( View view ) {
        ActivityService_.intent( this ).start();
    }
}
