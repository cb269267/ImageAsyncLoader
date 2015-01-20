package com.ct.image.imageasyncloader.i;

import android.graphics.drawable.BitmapDrawable;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public interface IImageCache {
    public void set(String key, BitmapDrawable data);

    public BitmapDrawable get(String key);

    public void clear();

    public void delete(String key);

    /**
     * max byte
     *
     * @return
     */
    public long getMaxSize();

    /**
     * used byte
     *
     * @return
     */
    public long getUsedSpace();
}
