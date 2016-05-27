package com.example.zcy.joker.base;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxFragment;

import javax.annotation.Nonnull;

/**
 * Created by zhangcaoyang on 16/5/27.
 */
public class BaseFragment extends RxFragment {

    public void showSnackMsg(@Nonnull View view, @Nonnull String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
