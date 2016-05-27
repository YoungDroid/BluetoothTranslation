package com.oom.translatecommunication.view.adapter;

import android.support.v7.widget.RecyclerView;

import com.oom.translatecommunication.R;
import com.oom.translatecommunication.app.CcBaseRecyclerAdapter;
import com.oom.translatecommunication.app.CcRecyclerHolder;
import com.oom.translatecommunication.model.TranslationMessage;

import java.util.Collection;

/**
 * Created by 小白杨 on 2016/3/10.
 */
public class AdapterTranslation extends CcBaseRecyclerAdapter< TranslationMessage > {

    public AdapterTranslation( RecyclerView v, Collection< TranslationMessage > datas, int itemLayoutId ) {
        super( v, datas, itemLayoutId );
    }

    @Override
    public void convert( CcRecyclerHolder holder, TranslationMessage item, int position, boolean isScrolling ) {
        holder.setText( R.id.tv_translation_time, item.getTime() );
        holder.setText( R.id.tv_translation_content, item.getContent() );
        switch ( item.getType() ) {
            case TranslationMessage.PhoneIn:
                holder.setText( R.id.tv_translation_type, "PhoneIn：" );
                holder.setText( R.id.tv_translation_title, item.getNumber() );
                break;
            case TranslationMessage.PhoneOut:
                holder.setText( R.id.tv_translation_type, "PhoneOut：" );
                holder.setText( R.id.tv_translation_title, item.getNumber() );
                break;
            case TranslationMessage.MessageIn:
                holder.setText( R.id.tv_translation_type, "MessageIn：" );
                holder.setText( R.id.tv_translation_title, item.getNumber() );
                break;
            case TranslationMessage.MessageOut:
                holder.setText( R.id.tv_translation_type, "MessageOut：" );
                holder.setText( R.id.tv_translation_title, item.getNumber() );
                break;
            case TranslationMessage.SystemInfo:
                holder.setText( R.id.tv_translation_type, "SystemInfo：" );
                break;
        }
    }
}
