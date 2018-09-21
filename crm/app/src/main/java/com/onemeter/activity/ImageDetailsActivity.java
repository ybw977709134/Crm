package com.onemeter.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.onemeter.R;
import com.onemeter.app.BaseActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 查看大图的Activity界面。(暂时不使用)
 *
 * @author guolin
 */
public class ImageDetailsActivity extends BaseActivity {

    ImageView imgView;
    Bitmap bitmap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取图片资源
        setContentView(R.layout.image_details);
        imgView = (ImageView) findViewById(R.id.imageView1);// 获取控件
        imgView.setOnTouchListener(new MulitPointTouchListener(imgView));
        imgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        String urls = "http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg";
        bitmap = returnBitMap(urls);
        if (null != bitmap) {
            imgView.setImageBitmap(bitmap);
        }


    }


    public Bitmap returnBitMap(String url) {

        if (null == url || "".equals(url)) {
            return null;
        }
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(2000);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }


    public class MulitPointTouchListener implements View.OnTouchListener {

        Matrix matrix = new Matrix();
        Matrix savedMatrix = new Matrix();

        public ImageView image;
        static final int NONE = 0;
        static final int DRAG = 1;
        static final int ZOOM = 2;
        int mode = NONE;

        PointF start = new PointF();
        PointF mid = new PointF();
        float oldDist = 1f;


        public MulitPointTouchListener(ImageView image) {
            super();
            this.image = image;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            this.image.setScaleType(ImageView.ScaleType.MATRIX);

            ImageView view = (ImageView) v;
//        dumpEvent(event);

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:

                    Log.w("FLAG", "ACTION_DOWN");
                    matrix.set(view.getImageMatrix());
                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.w("FLAG", "ACTION_POINTER_DOWN");
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.w("FLAG", "ACTION_UP");
                case MotionEvent.ACTION_POINTER_UP:
                    Log.w("FLAG", "ACTION_POINTER_UP");
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.w("FLAG", "ACTION_MOVE");
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        matrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);
                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist;
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                    break;
            }

            view.setImageMatrix(matrix);
            return true;
        }


        private float spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }

        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();   //回收图片所占的内存
                bitmap = null;
                System.gc();  //提醒系统及时回收
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();   //回收图片所占的内存
                bitmap = null;
                System.gc();  //提醒系统及时回收
            }
        }
    }
}