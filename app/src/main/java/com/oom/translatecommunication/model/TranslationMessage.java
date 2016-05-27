package com.oom.translatecommunication.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/27.
 */
public class TranslationMessage implements Serializable {
    public static final int PhoneIn = 1000;
    public static final int PhoneOut = 1001;
    public static final int MessageIn = 1002;
    public static final int MessageOut = 1003;
    public static final int SystemInfo = 1004;

    private String title;
    private String content;
    private String number;
    private String time;
    private int type;

    private JSONObject jsonMessage;

    public TranslationMessage() {
        initMessage();
    }

    public TranslationMessage(String content) {
        initMessage();
        this.content = content;
    }

    public TranslationMessage( JSONObject jsonParams ) {
        initMessage();
        try {
            title = jsonParams.getString( "title" );
            content = jsonParams.getString( "content" );
            number = jsonParams.getString( "number" );
            type = jsonParams.getInt( "type" );
        } catch ( JSONException e ) {
            new TranslationMessage();
        }
    }

    private void initMessage() {
        title = "";
        content = "";
        number = "";
        type = SystemInfo;
        Date date = new Date();
        DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:SS" );
        time = format.format( date );
        jsonMessage = new JSONObject();
        try {
            jsonMessage.put( "title", title );
            jsonMessage.put( "content", content );
            jsonMessage.put( "number", number );
            jsonMessage.put( "type", type );
            jsonMessage.put( "time", time );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
        try {
            jsonMessage.put( "title", title );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent( String content ) {
        this.content = content;
        try {
            jsonMessage.put( "content", content );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }

    public String getNumber() {
        return number;
    }

    public void setNumber( String number ) {
        this.number = number;
        try {
            jsonMessage.put( "number", number );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }

    public int getType() {
        return type;
    }

    public String getType( int type ) {
        switch ( type ) {
            case TranslationMessage.PhoneIn:
                return "PhoneIn：";
            case TranslationMessage.PhoneOut:
                return "PhoneOut：";
            case TranslationMessage.MessageIn:
                return "MessageIn：";
            case TranslationMessage.MessageOut:
                return "MessageOut：";
            case TranslationMessage.SystemInfo:
                return "SystemInfo：";
            default:
                return "SystemInfo：";
        }
    }

    public void setType( int type ) {
        this.type = type;
        try {
            jsonMessage.put( "type", type );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime( String time ) {
        this.time = time;
        try {
            jsonMessage.put( "time", time );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return jsonMessage.toString();
    }
}
