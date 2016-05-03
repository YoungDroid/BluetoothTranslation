package com.oom.translatecommunication.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.oom.translatecommunication.R;
import com.oom.translatecommunication.config.StaticVariables;
import com.oom.translatecommunication.utils.LocalDisplay;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.Timer;

/**
 * Created by 小白杨 on 2016/3/10.
 */
public class CcMagicTextView extends View {

    private String mText;
    private int mTextColor;
    private int mTextBackground;
    private int mTextSize;
    private int mTextOrientation;
    private int mTextMoveSpeed;

    private Rect mTextBounds;
    private Paint mTextPaint;

    private int textX;
    private boolean moveDirection = true;
    private Timer timerMove;

    public CcMagicTextView( Context context ) {
        this( context, null );
    }

    public CcMagicTextView( Context context, AttributeSet attrs ) {
        this( context, attrs, 0 );
    }

    public CcMagicTextView( Context context, AttributeSet attrs, int defStyleAttr ) {
        super( context, attrs, defStyleAttr );

        TypedArray array = context.getTheme().obtainStyledAttributes( attrs, R.styleable.CcMagicTextView, defStyleAttr, 0 );
        int count = array.getIndexCount();
        for ( int i = 0; i < count; i++ ) {
            int attr = array.getIndex( i );
            switch ( attr ) {
                case R.styleable.CcMagicTextView_CcMagicText:
                    mText = array.getString( attr );
                    break;
                case R.styleable.CcMagicTextView_CcMagicTextColor:
                    mTextColor = array.getColor( attr, Color.CYAN );
                    break;
                case R.styleable.CcMagicTextView_CcMagicTextBackground:
                    mTextBackground = array.getColor( attr, Color.TRANSPARENT );
                    break;
                case R.styleable.CcMagicTextView_CcMagicTextSize:
                    mTextSize = array.getDimensionPixelSize( attr, LocalDisplay.attrsSP2px( getResources() ) );
                    break;
                case R.styleable.CcMagicTextView_CcMagicTextOrientation:
                    // do'nt know how enum type get
                    break;
                case R.styleable.CcMagicTextView_CcMagicTextMoveSpeed:
                    mTextMoveSpeed = array.getInt( attr, 20 );
                    break;
            }
        }
        array.recycle();

        mTextPaint = new Paint();
        mTextPaint.setTextSize( mTextSize );
        mTextBounds = new Rect();
        mTextPaint.getTextBounds( mText, 0, mText.length(), mTextBounds );

        textX = getPaddingLeft();

        new Thread() {
            @Override
            public void run() {
                while ( true ) {
                    if ( moveDirection ) {
                        if ( textX < getMeasuredWidth() - mTextBounds.width() - getPaddingRight() ) {
                            textX++;
                        } else {
                            moveDirection = false;
                        }
                    } else {
                        if ( textX > getPaddingLeft() ) {
                            textX--;
                        } else {
                            moveDirection = true;
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep( mTextMoveSpeed );
                    } catch ( InterruptedException e ) {
                        e.printStackTrace();
                    }

                    if ( StaticVariables.DEBUG ) {
//                        Logger.init().setLogLevel( LogLevel.FULL );
//                        Logger.t( "Timer" ).d( "move" );
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onMeasure( int widthMeasureSpec, int heightMeasureSpec ) {
        int widthMode = MeasureSpec.getMode( widthMeasureSpec );
        int widthSize = MeasureSpec.getSize( widthMeasureSpec );
        int heightMode = MeasureSpec.getMode( heightMeasureSpec );
        int heightSize = MeasureSpec.getSize( heightMeasureSpec );

        int width, height;

        if ( widthMode == MeasureSpec.EXACTLY ) {
            width = widthSize;
        } else {
            mTextPaint.setTextSize( mTextSize );
            mTextPaint.getTextBounds( mText, 0, mText.length(), mTextBounds );
            float textWidth = mTextBounds.width();
            int desired = ( int ) ( getPaddingLeft() + textWidth + getPaddingRight() );
            width = desired;
        }

        if ( heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
        } else {
            mTextPaint.setTextSize( mTextSize );
            mTextPaint.getTextBounds( mText, 0, mText.length(), mTextBounds );
            float textHeight = mTextBounds.height();
            int desired = ( int ) ( getPaddingTop() + textHeight + getPaddingBottom() );
            height = desired;
        }

        setMeasuredDimension( width, height );
    }

    @Override
    protected void onDraw( Canvas canvas ) {

        mTextPaint.setColor( mTextBackground );
        canvas.drawRect( 0, 0, getMeasuredWidth(), getMeasuredHeight(), mTextPaint );

        mTextPaint.setColor( mTextColor );
        canvas.drawText( mText, textX, getHeight() / 2 + mTextBounds.height() / 4, mTextPaint );
    }
}
