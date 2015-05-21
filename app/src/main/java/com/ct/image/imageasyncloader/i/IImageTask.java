package com.ct.image.imageasyncloader.i;

/**
 * IImageTask
 *
 * @author tao.chen1
 */
public interface IImageTask extends Runnable {
    public String getKey();

    public void onFinished();

    public void onError();
}
