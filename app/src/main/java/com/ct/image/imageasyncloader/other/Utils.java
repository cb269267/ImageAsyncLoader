package com.ct.image.imageasyncloader.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utils
 *
 * @author tao.chen1
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

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

    /**
     * 读取本地图片，返回BitmapDrawable
     *
     * @param context 上下文
     * @param path    路径
     * @return BitmapDrawable
     */
    public static RecyclableBitmapDrawable readBitmapDrawable(Context context, String path) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        try {
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            int i = 0;
            while (true) {
                if ((opts.outWidth >> i <= Config.PIC_MAX_WIDTH)
                        && (opts.outHeight >> i <= Config.PIC_MAX_HEIGHT)) {
                    opts.inSampleSize = (int) Math.pow(2.0D, i);// 2.0^i
                    opts.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(path, opts);
                    break;
                }
                i += 1;
            }
        } catch (Throwable t) {
            if (Config.DEBUG) {
                Log.e(TAG, "e:", t);
            }
        }

        if (bitmap == null) {
            return null;
        } else {
            RecyclableBitmapDrawable bitmapDrawable = new RecyclableBitmapDrawable(context.getResources(), bitmap);
            bitmapDrawable = rotateBitmap(context, bitmapDrawable, path);
            return bitmapDrawable;
        }
    }

    public static RecyclableBitmapDrawable rotateBitmap(Context c, RecyclableBitmapDrawable bitmapDrawable, String path) {
        ExifInterface exi = null;
        int digree = 0;
        try {
            exi = new ExifInterface(path);
        } catch (IOException e) {
            exi = null;
            if (Config.DEBUG) {
                Log.e(TAG, "e:", e);
            }
        }

        if (exi != null) {
            int ori = exi.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }

        if (digree != 0) {
            Matrix m = new Matrix();
            m.postRotate(digree);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            bitmapDrawable = new RecyclableBitmapDrawable(c.getResources(), bitmap);
        }
        return bitmapDrawable;
    }

    public static int getInSampleSize(int width, int height, int maxWidth, int maxHeight) {
        int inSampleSize = 2;
        int tmpWidth = width;
        int tmpHeight = height;
        while (true) {
            tmpWidth /= 2;
            tmpHeight /= 2;
            if (tmpWidth <= maxWidth && tmpHeight <= maxHeight) {
                break;
            }
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] MD5(byte[] input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            if (Config.DEBUG) {
                Log.e(TAG, "e:", e);
            }
        }
        if (md != null) {
            md.update(input);
            return md.digest();
        } else {
            return null;
        }
    }

    public static String getMD5(byte[] input) {
        return Utils.bytesToHexString(MD5(input));
    }

    public static String getMD5(String input) {
        return getMD5(input.getBytes());
    }

    /**
     * Converts a byte array into a String hexidecimal characters null returns
     * null
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String table = "0123456789abcdef";
        int len = 2 * bytes.length;
        char[] cchars = new char[len];//不再使用StringBuilder,使用char数组优化速度，dmtrace 发现string.append最耗时
        for (int i = 0, k = 0; i < bytes.length; i++, k++) {
            int b;
            b = 0x0f & (bytes[i] >> 4);
            cchars[k] = table.charAt(b);
            b = 0x0f & bytes[i];
            k++;
            cchars[k] = table.charAt(b);
        }
        String sret = String.valueOf(cchars);
        cchars = null;
        return sret;
    }

    //TODO 效率可能不高
    public static boolean isUrl(String path) {
        try {
            URL url = new URL(path);
            if (Config.DEBUG) {
                Log.d(TAG, "是url路径");
            }
            return true;
        } catch (MalformedURLException e) {
            if (Config.DEBUG) {
                Log.d(TAG, "是本地file路径");
            }
        }
        return false;
    }
}
