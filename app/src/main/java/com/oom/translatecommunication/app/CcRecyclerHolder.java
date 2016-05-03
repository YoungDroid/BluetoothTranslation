package com.oom.translatecommunication.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/10/29.
 */
public class CcRecyclerHolder extends RecyclerView.ViewHolder {
    private final SparseArray< View > mViews;
    private final Context context;

    public CcRecyclerHolder( View itemView, Context context ) {
        super( itemView );
        //一般不会超过8个吧
        this.context = context;
        this.mViews = new SparseArray< View >( 8 );
    }

    public SparseArray< View > getAllView() {
        return mViews;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public < T extends View > T getView( int viewId ) {
        View view = mViews.get( viewId );
        if ( view == null ) {
            view = itemView.findViewById( viewId );
            mViews.put( viewId, view );
        }
        return ( T ) view;
    }

    public Context getContext() {
        return context;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public CcRecyclerHolder setText( int viewId, String text ) {
        TextView view = getView( viewId );
        view.setText( text );
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public CcRecyclerHolder setImageResource( int viewId, int drawableId ) {
        ImageView view = getView( viewId );
        view.setImageResource( drawableId );

        return this;
    }

    public CcRecyclerHolder setVisibility( int viewId, int visibility ) {
        View view = getView( viewId );
        view.setVisibility( visibility );
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public CcRecyclerHolder setImageBitmap( int viewId, Bitmap bm ) {
        ImageView view = getView( viewId );
        view.setImageBitmap( bm );
        return this;
    }

    public CcRecyclerHolder setImageResize( int viewId, int width, int height ) {
        ImageView view = getView( viewId );
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
        return this;
    }
}
