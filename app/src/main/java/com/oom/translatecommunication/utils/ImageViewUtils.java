package com.oom.translatecommunication.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/25.
 * Mail: xzlight@outlook.com
 */
public class ImageViewUtils {

    private static int IMAGE_MAX_WIDTH = LocalDisplay.SCREEN_WIDTH_PIXELS;

    private Map< String, SoftReference< Bitmap > > imageCache = new HashMap< String, SoftReference< Bitmap > >();

    public void addBitmapToCache( String path ) {
        // 强引用的Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeFile( path );
        // 软引用的Bitmap对象
        SoftReference< Bitmap > softBitmap = new SoftReference< Bitmap >( bitmap );
        // 添加该对象到Map中使其缓存
        imageCache.put( path, softBitmap );
    }

    public Bitmap getBitmapByPath( String path ) {
        // 从缓存中取软引用的Bitmap对象
        SoftReference< Bitmap > softBitmap = imageCache.get( path );
        // 判断是否存在软引用
        if ( softBitmap == null ) {
            return null;
        }
        // 取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空
        Bitmap bitmap = softBitmap.get();
        return bitmap;
    }

    public static void setMargins( ImageView view, int left, int top, int right, int bottom ) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        layoutParams.setMargins( left, top, right, bottom );
        view.setLayoutParams( layoutParams );
    }

//    public static void setFullScreen( ImageView view ) {
//        Bitmap ivBitmap;
//        //获得Bitmap的高和宽
//        ivBitmap = ( ( BitmapDrawable ) view.getDrawable() ).getBitmap().copy( Bitmap.Config.ARGB_4444, false );
//        int ivWidth = ivBitmap.getWidth();
//        int ivHeight = ivBitmap.getHeight();
//        //设置缩小比例
//        float scale = ( float ) LocalDisplay.SCREEN_WIDTH_PIXELS / ( float ) ivWidth;
//        //计算出这次要缩小的比例
//        //产生resize后的Bitmap对象
//        Matrix matrix = new Matrix();
//        matrix.postScale( scale, scale );
//        Bitmap resizeBmp = Bitmap.createBitmap( compressImage( ivBitmap ), 0, 0, ivWidth, ivHeight, matrix, true );
//        ivBitmap.recycle();
//        view.setImageBitmap( resizeBmp );
//    }

    public static Bitmap compressImage( Bitmap image ) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress( Bitmap.CompressFormat.JPEG, 100, baos );                              //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024 > 500 ) {                                   //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();                                                                   //重置baos即清空baos
            image.compress( Bitmap.CompressFormat.JPEG, options, baos );                      //这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;                                                                  //每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream( baos.toByteArray() );           //把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream( isBm, null, null );                       //把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static byte[] getImage( String path ) throws Exception {
        URL url = new URL( path );
        HttpURLConnection httpURLconnection = ( HttpURLConnection ) url.openConnection();
        httpURLconnection.setRequestMethod( "GET" );
        httpURLconnection.setReadTimeout( 6 * 1000 );
        InputStream in = null;
        byte[] b = new byte[ 1024 ];
        int len = -1;
        if ( httpURLconnection.getResponseCode() == 200 ) {
            in = httpURLconnection.getInputStream();
            byte[] result = readStream( in );
            in.close();
            return result;

        }
        return null;
    }

    public static byte[] readStream( InputStream in ) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[ 1024 ];
        int len = -1;
        while ( ( len = in.read( buffer ) ) != -1 ) {
            outputStream.write( buffer, 0, len );
        }
        outputStream.close();
        in.close();
        return outputStream.toByteArray();
    }
    
    public static void recycleImageView( ImageView view ) {
        if ( view != null && view.getDrawable() != null ) {
            Bitmap oldBitmap = ( ( BitmapDrawable ) view.getDrawable() ).getBitmap();
            view.setImageDrawable( null );
            if ( oldBitmap != null && !oldBitmap.isRecycled() ) {
                oldBitmap.recycle();
                oldBitmap = null;
                System.out.println( "GC_OK" );
                System.gc();
            }
        }
    }

    /**
     * 遍历所有view
     *
     * @param viewGroup
     */
    public static void traversalView( ViewGroup viewGroup ) {
        int count = viewGroup.getChildCount();
        for ( int i = 0; i < count; i++ ) {
            View view = viewGroup.getChildAt( i );
            if ( view instanceof ViewGroup ) {
                traversalView( ( ViewGroup ) view );
            } else {
                // do something
            }
        }
    }

    public static Bitmap getBitmapFromUri( Context context, Uri uri ) {
        Bitmap mBitmap = null;
        //将图片内容解析成字节数组
        try {
            byte[] mContent;
            ContentResolver resolver = context.getContentResolver();

            mContent = readStream( resolver.openInputStream( uri ) );
            //将字节数组转换为ImageView可调用的Bitmap对象
            mBitmap = getPicFromBytes( mContent, null );

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    public static Bitmap getPicFromBytes( byte[] bytes, BitmapFactory.Options opts ) {
        if ( bytes != null ) if ( opts != null ) return BitmapFactory.decodeByteArray( bytes, 0, bytes.length, opts );
        else return BitmapFactory.decodeByteArray( bytes, 0, bytes.length );
        return null;
    }
}
