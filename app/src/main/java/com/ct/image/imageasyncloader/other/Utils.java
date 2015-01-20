package com.ct.image.imageasyncloader.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import java.io.File;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public class Utils {

    public static File getExternalCacheDir(Context context) {
        File path = context.getExternalCacheDir();

        // In some case, even the sd card is mounted, getExternalCacheDir will return null, may be it is nearly full.
        if (path != null) {
            return path;
        }

        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
    }

    public static boolean isSDCardMounted() {
        String state = Environment.getExternalStorageState();
        if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static BitmapDrawable readBitmapDrawable(Context context, String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;

        try {
            opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            opts.inSampleSize = getInSampleSize(width, height, 600, 600);
            bitmap = BitmapFactory.decodeFile(path, opts);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (bitmap == null) {
            return null;
        } else {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
            return bitmapDrawable;
        }
    }

    public static int getInSampleSize(int width, int height, int maxWidth, int maxHeight) {
        int inSampleSize = 2;
        int tmpWidth = width;
        int tmpHeight = height;
        while (true) {
            tmpWidth /= 2;
            tmpHeight /=2;
            if (tmpWidth <= maxWidth && tmpHeight <= maxHeight) {
                break;
            }
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

}
