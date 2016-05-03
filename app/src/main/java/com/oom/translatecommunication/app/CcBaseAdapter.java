package com.oom.translatecommunication.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2015/10/29.
 */
public abstract class CcBaseAdapter<T> extends BaseAdapter implements AbsListView.OnScrollListener {
    protected Collection<T> mDatas;
    protected final int mItemLayoutId;
    protected AbsListView mList;
    protected boolean isScrolling;
    protected Context mCxt;
    protected LayoutInflater mInflater;

    private AbsListView.OnScrollListener listener;

    public CcBaseAdapter( AbsListView view, Collection<T> mDatas, int itemLayoutId) {
        if (mDatas == null) {
            mDatas = new ArrayList<T>(0);
        }
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.mList = view;
        mCxt = view.getContext();
        mInflater = LayoutInflater.from(mCxt);
        mList.setOnScrollListener(this);
    }

    public void refresh(Collection<T> datas) {
        if (datas == null) {
            datas = new ArrayList<T>(0);
        }
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addOnScrollListener(AbsListView.OnScrollListener l) {
        this.listener = l;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (mDatas instanceof List) {
            return ((List<T>) mDatas).get(position);
        } else if (mDatas instanceof Set) {
            return new ArrayList<T>(mDatas).get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CcAdapterHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position), position, isScrolling);
        return viewHolder.getConvertView();

    }

    private CcAdapterHolder getViewHolder( int position, View convertView,
                                           ViewGroup parent) {
        return CcAdapterHolder.get(convertView, parent, mItemLayoutId, position);
    }

    /**
     * ListView适配器填充方法
     *
     * @param holder      viewholder
     * @param item        javabean
     * @param isScrolling RecyclerView是否正在滚动
     */
    public abstract void convert( CcAdapterHolder holder, T item, int position, boolean isScrolling);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 设置是否滚动的状态
//        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//            isScrolling = false;
//            this.notifyDataSetChanged();
//        } else {
//            isScrolling = true;
//        }
//        if (listener != null) {
//            listener.onScrollStateChanged(view, scrollState);
//        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
//        if (listener != null) {
//            listener.onScroll(view, firstVisibleItem, visibleItemCount,
//                    totalItemCount);
//        }
    }
}
