package com.ct.image.imageasyncloader.impl;

import android.os.Environment;

import com.ct.image.imageasyncloader.i.IFileCacheManager;

/**
 * CustomFileCacheManager
 */
public class DefaultFileCacheManager extends IFileCacheManager {

    private static final String DIR_ROOT = "com.ct.image/";
    private static final String DIR_IMG = "img/";

    @Override
    public String getBasePath() {
        return getCommonRootPath();
    }

    private String getCommonRootPath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory().getPath() + "/" + DIR_ROOT + DIR_IMG;
        } else {
            return "sdcard/" + DIR_ROOT + DIR_IMG;
        }
    }
}
