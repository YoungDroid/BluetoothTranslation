package com.oom.translatecommunication.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/10/29.
 */
public class CcAdapterHolder {
    private final SparseArray< View > mViews;
    private final int mPosition;
    private final View mConvertView;
    private final Context context;

    private CcAdapterHolder( ViewGroup parent, int layoutId, int position ) {
        this.mPosition = position;
        this.mViews = new SparseArray< View >();
        this.context = parent.getContext();
        mConvertView = LayoutInflater.from( context ).inflate( layoutId, parent, false );
        // setTag
        mConvertView.setTag( this );
    }

    /**
     * 拿到全部View
     *
     * @return
     */
    public SparseArray< View > getAllView() {
        return mViews;
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static CcAdapterHolder get( View convertView, ViewGroup parent, int layoutId, int position ) {
        if ( convertView == null ) {
            return new CcAdapterHolder( parent, layoutId, position );
        } else {
            return ( CcAdapterHolder ) convertView.getTag();
        }
    }

    public View getConvertView() {
        return mConvertView;
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
            view = mConvertView.findViewById( viewId );
            mViews.put( viewId, view );
        }
        return ( T ) view;
    }

    public CcAdapterHolder setVisibility( int viewId, int visibility ) {
        View view = getView( viewId );
        view.setVisibility( visibility );
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public CcAdapterHolder setText( int viewId, CharSequence text ) {
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
    public CcAdapterHolder setImageResource( int viewId, int drawableId ) {
        ImageView view = getView( viewId );
        view.setImageResource( drawableId );

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public CcAdapterHolder setImageBitmap( int viewId, Bitmap bm ) {
        ImageView view = getView( viewId );
        view.setImageBitmap( bm );
        return this;
    }

    public CcAdapterHolder setImageResize( int viewId, int width, int height ) {
        ImageView view = getView( viewId );
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

    public Context getContext() {
        return context;
    }
}
