package com.ct.image.imageasyncloader.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.toolbox.Volley;
import com.ct.image.imageasyncloader.core.ImageLoader;
import com.ct.image.imageasyncloader.impl.DownloadImageTask;
import com.ct.image.imageasyncloader.impl.ImageTask;
import com.ct.image.imageasyncloader.impl.LoadImageTask;
import com.ct.image.imageasyncloader.other.RecyclableBitmapDrawable;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public class CustomImageView extends ImageView {

    private String mImageUrl = null;

    private String mImagePath = null;

    private ImageTask mCurrentTask = null;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        final Drawable previousDrawable = getDrawable();
        super.setImageDrawable(drawable);
        updateDrawableState(previousDrawable, false);
        updateDrawableState(drawable, true);
    }

    private void updateDrawableState(Drawable drawable, boolean isDisplayed){
        if (drawable instanceof RecyclableBitmapDrawable) {
            ((RecyclableBitmapDrawable) drawable).updateDisplayState(isDisplayed);
        }
    }

    public void loadImage() {
        if (TextUtils.isEmpty(mImagePath) && TextUtils.isEmpty(mImageUrl)) {
            return;
        }

        if (!TextUtils.isEmpty(mImagePath)) {
            //load image from file
            LoadImageTask task = new LoadImageTask(mImagePath);
            ImageLoader.getInstance().addImageTask(task, this);
        } else if (!TextUtils.isEmpty(mImageUrl)) {
            //load image on the internet
            DownloadImageTask task = new DownloadImageTask(mImageUrl, ImageLoader.getInstance().getRequestQueue(getContext()));
            ImageLoader.getInstance().addImageTask(task, this);
        }
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String imagePath) {
        this.mImageUrl = null;
        this.mImagePath = imagePath;
    }

    public ImageTask getCurrentTask() {
        return mCurrentTask;
    }

    public void setCurrentTask(ImageTask task) {
        this.mCurrentTask = task;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.mImagePath = null;
        this.mImageUrl = ImageUrl;
    }
}
