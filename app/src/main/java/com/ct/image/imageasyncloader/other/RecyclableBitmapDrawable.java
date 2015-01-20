package com.ct.image.imageasyncloader.other;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public class RecyclableBitmapDrawable extends BitmapDrawable {

    private int mRefCacheCount = 0;
    private int mRefDisplayCount = 0;
    private boolean mHasBeenDisplayed = false;

    private static final byte[] LOCK = new byte[0];

    public RecyclableBitmapDrawable(Resources res, Bitmap bitmap) {
        super(res, bitmap);
    }

    public void updateDisplayState(boolean isDisplayed) {
        synchronized (LOCK) {
            if (isDisplayed) {
                mRefDisplayCount++;
                mHasBeenDisplayed = true;
            } else {
                mRefDisplayCount--;
            }
            checkState();
        }
    }

    public void updateCacheState(boolean isCached) {
        synchronized (this) {
            if (isCached) {
                mRefCacheCount++;
            } else {
                mRefCacheCount--;
            }
        }
        checkState();
    }

    /**
     * check if is necessary to call recycle bitmap
     */
    private void checkState() {
        if (mRefCacheCount <= 0 && mRefDisplayCount <= 0 && mHasBeenDisplayed && isBitmapValid()) {
            getBitmap().recycle();
        }
    }

    private boolean isBitmapValid() {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }
}
