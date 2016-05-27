package com.example.zcy.joker.jokers;

import android.content.Context;
import android.util.Log;

import com.example.zcy.joker.jokers.data.JokerEntity;
import com.example.zcy.joker.jokers.data.JokerModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by zcy on 2016/5/3.
 */
public class JokerPresenter implements JokerContract.Presenter {
    private JokerContract.View jokersView;
    private JokerModel jokerModel;
    private Context context;

    public JokerPresenter(@Nonnull JokerModel jokerModel, @Nonnull JokerContract.View jokersView
            , @Nonnull Context context) {
        this.jokersView = jokersView;
        this.jokerModel = jokerModel;
        this.context = context;
        jokersView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!jokersView.isActive()) {
            return;
        }
        if (jokerModel.jokers == null || jokerModel.jokers.size() == 0) {
            getJokersData();
        } else {
            jokersView.showJokers(jokerModel.jokers);
        }
    }

    @Override
    public void getJokersData() {
        jokersView.showLoading();
        jokerModel.getJokersData(false, new JokerContract.OnGetDataListener() {

            @Override
            public void onSuccess(@Nonnull List<JokerEntity> jokers) {
                jokersView.showJokers(jokerModel.jokers);
            }

            @Override
            public void onFailed() {
                jokersView.showError();
            }
        });
    }

    @Override
    public void getMoreJokersData() {
        jokerModel.getJokersData(true, new JokerContract.OnGetDataListener() {

            @Override
            public void onSuccess(@Nonnull List<JokerEntity> jokers) {
                jokersView.showLoadMoreSuccess(jokers);
            }

            @Override
            public void onFailed() {
                jokersView.showLoadMoreFailed();
            }
        });
    }

    @Override
    public void refreshJokersData() {
        jokerModel.getJokersData(false, new JokerContract.OnGetDataListener() {

            @Override
            public void onSuccess(@Nonnull List<JokerEntity> jokers) {
                jokersView.hideRefreshLayout();
                jokersView.showJokers(jokers);
            }

            @Override
            public void onFailed() {
                jokersView.showRefreshFailed();
            }
        });
    }
}
