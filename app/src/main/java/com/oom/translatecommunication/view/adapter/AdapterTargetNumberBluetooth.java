package com.oom.translatecommunication.view.adapter;

import android.support.v7.widget.RecyclerView;

import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseRecyclerAdapter;
import com.oom.translatecommunication.app.CcRecyclerHolder;

import java.util.Collection;

/**
 * Created by 小白杨 on 2016/3/10.
 */
public class AdapterTargetNumberBluetooth extends CcBaseRecyclerAdapter< String > {

    public AdapterTargetNumberBluetooth( RecyclerView v, Collection< String > datas, int itemLayoutId ) {
        super( v, datas, itemLayoutId );
    }

    @Override
    public void convert( CcRecyclerHolder holder, String item, int position, boolean isScrolling ) {
        holder.setText( R.id.tv_target_number_bluetooth_list_item, item );
    }
}
