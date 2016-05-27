package com.example.zcy.joker.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jude.utils.JActivityManager;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by zcy on 2016/5/3.
 */
public class BaseActivity extends RxAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JActivityManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JActivityManager.getInstance().popActivity(this);
    }
}
