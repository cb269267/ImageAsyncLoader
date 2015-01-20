package com.ct.image.imageasyncloader.i;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public interface LoadImageCallback {
    public void OnStart();
    public void OnError(int errCode);
    public void OnFinished(BitmapDrawable result);
}
