package com.jcb.base_corelibrary.utils.image;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.jcb.base_corelibrary.R;

/**
 * Created by jinchangbo on 20-11-2.
 */
public class ImageUtils {

    public static void loadImage(Context context, ImageView imageView, String url) {
        loadImage(context, imageView, url, new RoundCorner(context, 0, 0, 0, 0));
    }

    public static void loadImage(Context context, ImageView imageView, String url, BitmapTransformation transformation) {
        if (imageView == null || TextUtils.isEmpty(url))
            return;

        Glide.with(context)
                .load(url)
                .thumbnail(0.2f)//缩略图
                .placeholder(R.drawable.placeholder_pic)//图片加载出来前，显示的图片
                .error(R.drawable.placeholder_pic)//图片加载失败后，显示的图片
                .apply(RequestOptions.bitmapTransform(transformation)) //圆角显示
                .into(imageView);
    }

}
