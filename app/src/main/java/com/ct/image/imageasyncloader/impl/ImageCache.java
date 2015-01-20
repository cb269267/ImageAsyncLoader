package com.ct.image.imageasyncloader.impl;

import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.ct.image.imageasyncloader.i.IImageCache;
import com.ct.image.imageasyncloader.other.ImageUtils;
import com.ct.image.imageasyncloader.other.RecyclableBitmapDrawable;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public class ImageCache implements IImageCache {

    private LruCache<String, BitmapDrawable> mMemoryCache;
    private static volatile ImageCache sInstance = null;

    /**
     * custom image cache with specific memory size
     * @param size image cache memory size
     * @return ImageCache
     */
    public static ImageCache getInstance(int size){
        if (sInstance == null) {
            synchronized (ImageCache.class) {
                if(sInstance == null) {
                    sInstance = new ImageCache(size);
                }
            }
        }
        return sInstance;
    }

    /**
     * default cache size: 20% max memory
     * @return ImageCache
     */
    public static ImageCache getsInstance(){
        int size = Math.round(0.2f * Runtime.getRuntime().maxMemory() / 1024);
        Log.e("fuck", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "");
        return getInstance(size);
    }

    /**
     *
     * @param cacheSize kb
     */
    private ImageCache(int cacheSize) {
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {

            /**
             * Notify the removed entry that is no longer being cached
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (RecyclableBitmapDrawable.class.isInstance(oldValue)) {
                    // The removed entry is a recycling drawable, so notify it
                    // that it has been removed from the memory cache
                    ((RecyclableBitmapDrawable) oldValue).updateCacheState(false);
                } else {
                    // The removed entry is a standard BitmapDrawable
                    // do nothing
                }
            }

            /**
             * Measure item size in kilobytes rather than units which is more practical for a bitmap cache
             */
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                final int bitmapSize = ImageUtils.getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
            }
        };

        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }

            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return super.sizeOf(key, value);
            }
        };
    }

    @Override
    public void set(String key, BitmapDrawable data) {
        if (key == null || data == null) {
            return;
        }

        // Add to memory cache
        if (mMemoryCache != null) {
            if (RecyclableBitmapDrawable.class.isInstance(data)) {
                // The removed entry is a recycling drawable, so notify it
                // that it has been added into the memory cache
                ((RecyclableBitmapDrawable) data).updateCacheState(true);
            }
            mMemoryCache.put(key, data);
        }
    }

    @Override
    public BitmapDrawable get(String key) {
        BitmapDrawable memValue = null;
        if (mMemoryCache != null) {
            memValue = mMemoryCache.get(key);
        }
        return memValue;
    }

    @Override
    public void clear() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }
    }

    @Override
    public void delete(String key) {
        mMemoryCache.remove(key);
    }

    @Override
    public long getMaxSize() {
        return mMemoryCache.maxSize() * 1024;
    }

    @Override
    public long getUsedSpace() {
        return mMemoryCache.size() * 1024;
    }
}
