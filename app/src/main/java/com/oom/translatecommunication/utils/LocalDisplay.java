package com.oom.translatecommunication.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class LocalDisplay {

    public static int SCREEN_WIDTH_PIXELS;
    public static int SCREEN_HEIGHT_PIXELS;
    public static float SCREEN_DENSITY;
    public static int SCREEN_WIDTH_DP;
    public static int SCREEN_HEIGHT_DP;

    public static void init(DisplayMetrics dm) {

        SCREEN_WIDTH_PIXELS = dm.widthPixels;
        SCREEN_HEIGHT_PIXELS = dm.heightPixels;
        SCREEN_DENSITY = dm.density;
        SCREEN_WIDTH_DP = (int) (SCREEN_WIDTH_PIXELS / dm.density);
        SCREEN_HEIGHT_DP = (int) (SCREEN_HEIGHT_PIXELS / dm.density);
        Log.d("LOCAL_DISPLAY", "INIT APP " + " width: " + LocalDisplay.SCREEN_WIDTH_PIXELS +
                " height: " + LocalDisplay.SCREEN_HEIGHT_PIXELS);
    }

    public static int dp2px(float dp) {
        final float scale = SCREEN_DENSITY;
        return (int) (dp * scale + 0.5f);
    }

    public static int designedDP2px(float desingDP) {
        if (SCREEN_WIDTH_DP != 320) {
            desingDP = desingDP * SCREEN_WIDTH_DP / 320f;
        }
        return dp2px(desingDP);
    }

    public static int attrsSP2px( Resources resources) {
        return ( int ) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_SP, 16, resources.getDisplayMetrics() );
    }

    public static void setPadding(final View view, float left, float top, float right, float bottom) {
        view.setPadding(designedDP2px(left), dp2px(top), designedDP2px(right), dp2px(bottom));
    }

    public static void setMargins(final View view, float left, float top, float right, float bottom) {
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lllp.setMargins(designedDP2px(left), (int)(top), designedDP2px(right), dp2px(bottom));
        view.setLayoutParams(lllp);
    }
}
