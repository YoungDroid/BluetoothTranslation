package com.oom.translatecommunication.app;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;

/**
 * @author 小白杨
 * @version 1.3 Edit on 2016-02-03
 */
public abstract class CcBaseFragmentActivity extends FragmentActivity {

    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;

    public int activityState;
    public ApplicationTranslateCommunication application;

    // ExitApp Time
    public long exitAppTime;

    // 是否允许全屏
    private boolean mAllowFullScreen = false;

    public abstract void initWidget();


    public abstract void initData();

    public abstract void loadData();

    public void setAllowFullScreen( boolean allowFullScreen ) {
        this.mAllowFullScreen = allowFullScreen;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        JUtils.Log( this.getClass().getSimpleName(), "---------onCreate " );
        // 竖屏锁定
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        if ( mAllowFullScreen ) {
            requestWindowFeature( Window.FEATURE_NO_TITLE ); // 取消标题
        }
        JActivityManager.getInstance().pushActivity( this );

        application = ApplicationTranslateCommunication.getInstance();

        initData();
        initWidget();
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        JUtils.Log( this.getClass().getSimpleName(), "---------onStart " );
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
}
