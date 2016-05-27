package com.example.zcy.joker;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.jude.utils.JUtils;

/**
 * Created by zhangcaoyang on 16/5/19.
 */
public class JokerApplication extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        JUtils.initialize(this);
        Stetho.initializeWithDefaults(this);
    }
}
