package com.ct.image.imageasyncloader.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public class DownloadImageTask extends ImageTask {
    private static final String TAG = "DownloadImageTask";
    private String mDownloadUrl = "";
    private RequestQueue mDownloadReqQueue = null;

    public DownloadImageTask(String url, RequestQueue downloadReqQueue) {
        mDownloadUrl = url;
        mDownloadReqQueue = downloadReqQueue;
    }

    @Override
    public String getKey() {
        if (TextUtils.isEmpty(mDownloadUrl)) {
            return null;
        } else {
            return mDownloadUrl;
        }
    }

    @Override
    public void doInBackground() {
        if (mDownloadReqQueue != null) {
            ImageRequest imgReq = new ImageRequest(mDownloadUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    if (mImageViewList.size() > 0) {
                        Context context = mImageViewList.get(0).getContext();
                        result = new BitmapDrawable(context.getResources(), bitmap);
                    }
                    onFinished();
                }
            }, 600, 600, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    onError();
                }
            });
            if (mDownloadReqQueue != null) {
                mDownloadReqQueue.add(imgReq);
            }
        } else {
            Log.e(TAG, "download failed, no request queue");
        }
    }
}
