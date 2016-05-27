package com.example.zcy.joker.base;

import javax.annotation.Nonnull;

/**
 * Created by zcy on 2016/5/3.
 */
public interface BaseView<T> {
    void setPresenter(@Nonnull T presenter);
}
