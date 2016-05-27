package com.example.zcy.joker.jokers;

import com.example.zcy.joker.base.BasePresenter;
import com.example.zcy.joker.base.BaseView;
import com.example.zcy.joker.jokers.data.JokerEntity;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by zcy on 2016/5/3.
 */
public interface JokerContract {
    interface View extends BaseView<Presenter> {
        void showJokers(@Nonnull List<JokerEntity> jokers);

        void showLoading();

        void showError();

        void showLoadMoreFailed();

        void showLoadMoreSuccess(@Nonnull List<JokerEntity> jokers);

        void hideRefreshLayout();

        void showRefreshFailed();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void getJokersData();

        void getMoreJokersData();

        void refreshJokersData();

    }

    interface OnGetDataListener {
        void onSuccess(@Nonnull List<JokerEntity> jokers);

        void onFailed();
    }
}
