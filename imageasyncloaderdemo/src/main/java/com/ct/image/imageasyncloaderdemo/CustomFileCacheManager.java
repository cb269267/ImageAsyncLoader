package com.ct.image.imageasyncloaderdemo;

import android.os.Environment;

import com.ct.image.imageasyncloader.i.IFileCacheManager;

/**
 * CustomFileCacheManager
 */
public class CustomFileCacheManager extends IFileCacheManager {

    private static final String DIR_ROOT = "com.ct.image/img";
    private static final String DIR_1 = "subdir1/";
    private static final String DIR_2 = "subdir2/";
    private static final String DIR_3 = "subdir3/";
    private static final String DIR_4 = "subdir4/";
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;
    public static final int TYPE_4 = 4;

    private int type = 1;

    public CustomFileCacheManager(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getBasePath() {
        String rootPath = getCommonRootPath();
        String path = "";
        switch (type) {
            case TYPE_1:
                path = rootPath + DIR_1;
                break;
            case TYPE_2:
                path = rootPath + DIR_2;
                break;
            case TYPE_3:
                path = rootPath + DIR_3;
                break;
            case TYPE_4:
                path = rootPath + DIR_4;
                break;
        }
        return path;
    }

    private String getCommonRootPath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory().getPath() + "/" + DIR_ROOT;
        } else {
            return "sdcard/" + DIR_ROOT;
        }
    }
}
