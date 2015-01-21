package com.ct.image.imageasyncloader.i;

/**
 * Created by tao.chen1 on 2015/1/15.
 */
public interface IImageTask extends Runnable{
    public String getKey();
    public void onFinished();
    public void onError();
}
