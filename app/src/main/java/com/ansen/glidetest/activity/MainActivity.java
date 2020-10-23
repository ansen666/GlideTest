package com.ansen.glidetest.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.ansen.glidetest.R;
import com.ansen.glidetest.app.GlideApp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private ImageView ivCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageView=findViewById(R.id.imageview);
        final ImageView ivGif=findViewById(R.id.iv_gif);

        //加载网络图片
//        Glide.with(this)
//                .load("https://github.com/ansen666/images/blob/master/" +
//                  "public/qrcode_for_gh_14a89f21bd5e_258.jpg?raw=true")
//                .into(imageView);

        //加载gif图片
        Glide.with(this).asGif().load(R.mipmap.result).into(ivGif);

        //开启后台线程获取Bitmap
        new Thread(runnable).start();

        //主线程异步获取Bitmap
//        Glide.with(this)
//            .asBitmap()
//            .load("https://github.com/ansen666/images/blob/master/" +
//                    "public/qrcode_for_gh_14a89f21bd5e_258.jpg?raw=true")
//            .into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                    //这里我们拿到回掉回来的bitmap，可以加载到我们想使用到的地方
//                    imageView.setImageBitmap(resource);
//                }
//
//                @Override
//                public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                    super.onLoadFailed(errorDrawable);
//                    Log.i("ansen", "图片加载失败");
//                }
//          });

        //Generated API 使用方式
        GlideApp.with(this)
                .load("https://github.com/ansen666/images/blob/master/" +
                        "public/qrcode_for_gh_14a89f21bd5e_258.jpg?raw=true")
                .into(imageView);

        ivCircle=findViewById(R.id.iv_circle);

        Glide.with(this).asBitmap().load("https://mo-development.oss-cn-hangzhou.aliyuncs.com/users/avatar/13146666/5f46200b1e5d7.jpg@!small").apply(RequestOptions.bitmapTransform(new CircleCrop())).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                ivCircle.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
            }
        });
    }

    //在后台线程中获取bitmap
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            FutureTarget<Bitmap> futureTarget =
                    Glide.with(MainActivity.this)
                            .asBitmap()
                            .load("https://github.com/ansen666/images/blob/master/" +
                                    "public/qrcode_for_gh_14a89f21bd5e_258.jpg?raw=true")
                            .submit();
            try {
                Bitmap bitmap = futureTarget.get();
                Log.i("ansen","获取的bitmap:"+bitmap);

                //当上面获取的bitmap使用完毕时，调用clear方法释放资源
                Glide.with(MainActivity.this).clear(futureTarget);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
