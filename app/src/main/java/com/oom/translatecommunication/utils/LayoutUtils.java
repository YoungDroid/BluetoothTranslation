package com.oom.translatecommunication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2015/9/15.
 * Mail: xzlight@outlook.com
 */
public class LayoutUtils {

    public static Drawable LayoutToDrawable(Context context, int layout_id) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout_id, null);
        Bitmap snapshot = convertViewToBitmap(view);
        Drawable drawable = new BitmapDrawable(snapshot);
        return drawable;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
