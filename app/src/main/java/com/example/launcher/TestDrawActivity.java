package com.example.launcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Author:mihon
 * Time: 2019\2\26 0026.16:11
 * Description:This is TestDrawActivity
 */
public class TestDrawActivity extends AppCompatActivity {
    RelativeLayout rootview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        rootview = findViewById(R.id.rootview);
        String pathName;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.demo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootview.setBackground(new BitmapDrawable(getResources(),bitmap));
        }
//    readColor(bitmap);

    }
    private void readColor2(Bitmap bitmap){
        //循环获得bitmap所有像素点
        int mBitmapWidth = bitmap.getWidth();
        int mBitmapHeight = bitmap.getHeight();
//        for (int i = 0; i < mBitmapHeight; i++) {
//            for (int j = 0; j < mBitmapWidth; j++) {
//
//            }
//        }
        Bitmap mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 1360; i < 1370; i++) {
                                                                   for (int j = 570; j < 580; j++) {
                Log.i("222222","color:"+Integer.toHexString(mBitmap.getPixel(j,i)));
                mBitmap.setPixel(j, i, 0xffff0000);  //红点
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootview.setBackground(new BitmapDrawable(getResources(),mBitmap));
        }

    }
    private void readColor(Bitmap bitmap){
        //循环获得bitmap所有像素点
        int mBitmapWidth = bitmap.getWidth();
        int mBitmapHeight = bitmap.getHeight();
//        for (int i = 0; i < mBitmapHeight; i++) {
//            for (int j = 0; j < mBitmapWidth; j++) {
//
//            }
//        }
        Bitmap mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < mBitmapWidth; i++) {
            for (int j = 0; j < mBitmapHeight; j++) {
//                Log.i("222222","color:"+Integer.toHexString(mBitmap.getPixel(j,i)));
                if(mBitmap.getPixel(i,j)!=0xff5dcc99){
                    mBitmap.setPixel(i, j, 0xfffffff);  //红点
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootview.setBackground(new BitmapDrawable(getResources(),mBitmap));
        }

    }
    public static Bitmap replaceBitmapColor(Bitmap oldBitmap,int oldColor,int newColor)
    {
        //相关说明可参考 http://xys289187120.blog.51cto.com/3361352/657590/
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int[] mArrayColor = new int[mArrayColorLengh];
        int count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                //将需要填充的颜色值如果不是
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                int color = mBitmap.getPixel(j, i);
                //将颜色值存在一个数组中 方便后面修改
                if (color == oldColor) {
                    mBitmap.setPixel(j, i, newColor);  //将白色替换成透明色
                }

            }
        }
        return mBitmap;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }
}

