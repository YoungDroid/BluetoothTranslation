package com.oom.translatecommunication.app;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;
import com.oom.translatecommunication.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * 应用程序Activity的基类
 *
 * @author 小白杨
 * @version 1.3 Edit on 2016-02-03
 */
@EActivity
public abstract class CcBaseActivity extends AppCompatActivity {

    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;
    private static final int ACTIVITY_FINISH = 4;

    private View baseView;
    protected Toolbar mToolbar;
    private LinearLayout llContainer;

    public int activityState;
    public ApplicationTranslateCommunication application;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    // 是否允许全屏
    private boolean mAllowFullScreen = false;

    public abstract String tag();

    public abstract void initView();

    public abstract void initPresenter();

    public abstract void initOtherThing();

    public void setAllowFullScreen( boolean allowFullScreen ) {
        this.mAllowFullScreen = allowFullScreen;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        initBaseView( getLayoutInflater(), null );
        super.onCreate( savedInstanceState );
        JUtils.Log( this.getClass().getSimpleName(), "---------onCreate " );
        // 竖屏锁定
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        if ( mAllowFullScreen ) {
            requestWindowFeature( Window.FEATURE_NO_TITLE ); // 取消标题
        }
        // 透明ActionBar 和 顶部导航栏
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS );
            // Translucent navigation bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        JActivityManager.getInstance().pushActivity( this );

        application = ApplicationTranslateCommunication.getInstance();
        sharedPreferences = getSharedPreferences( "BluetoothTranslation", 0 );
        editor = sharedPreferences.edit();
    }

    private void initBaseView( LayoutInflater inflater, ViewGroup container ) {
        baseView = inflater.inflate( R.layout.base_activity_layout, container, false );
        mToolbar = ( Toolbar ) baseView.findViewById( R.id.toolbar );
        mToolbar.setBackgroundColor( Color.BLACK );
        mToolbar.setTitle( tag() );
        setSupportActionBar( mToolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        mToolbar.setNavigationIcon( R.mipmap.iv_toobar_back );
        mToolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                finish();
            }
        } );
        llContainer = ( LinearLayout ) baseView.findViewById( R.id.ll_base_activity_container );
    }

    @Override
    public void setContentView( int layoutResID ) {
        llContainer.addView( getLayoutInflater().inflate( layoutResID, null ) );
        super.setContentView( baseView );
    }

    @Override
    public void setContentView( View view ) {
        llContainer.addView( view );
        super.setContentView( baseView );
    }

    @AfterViews
    public void afterViews() {
        initView();
        initOtherThing();
        initPresenter();
        refresh();
    }

    public void refresh() {}

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            // exitActivity
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ACTIVITY_RESUME;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = ACTIVITY_STOP;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = ACTIVITY_PAUSE;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityState = ACTIVITY_DESTROY;
    }

    @Override
    public void finish() {
        super.finish();
        activityState = ACTIVITY_FINISH;
    }

    public boolean isBackClick = false;

    public void back( View v ) {
        if ( !isBackClick ) {
            isBackClick = true;
            // exitActivity
            finish();
        }
    }
}