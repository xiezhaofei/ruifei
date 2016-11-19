package com.ruifei.technology.android.data.source;

/**
 * Created by zhaofei.xie on 2016/10/5.
 */
public class PullData {
    public String title;
    public String sig;
    public int pic;
    public PullData(){};
    public PullData(String title,String sig,int pic)
    {
        this.title = title;
        this.sig = sig;
        this.pic = pic;
    }
}
