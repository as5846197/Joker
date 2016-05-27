package com.example.zcy.joker.jokers.data;

import android.content.Context;
import android.util.Log;

import com.example.zcy.joker.jokers.JokerContract;
import com.example.zcy.joker.network.NetworkController;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import rx.Subscriber;

/**
 * Created by zcy on 2016/5/3.
 */
public class JokerModel {

    public List<JokerEntity> jokers;
    public int page = 1;

    private Context context;
    private String pageSize = "10";

    public JokerModel(Context context) {
        this.context = context;
    }

    public void getJokersData(final boolean isLoadMore, @Nonnull final JokerContract.OnGetDataListener listener) {
        page = isLoadMore ? ++page : 1;
        NetworkController.getInstance().getJokerList(page, pageSize)
                .subscribe(new Subscriber<List<JokerEntity>>() {
                    @Override
                    public void onCompleted() {
                        listener.onSuccess(jokers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailed();
                    }

                    @Override
                    public void onNext(List<JokerEntity> jokerEntities) {
                        if (isLoadMore) {
                            jokers.addAll(jokerEntities);
                        } else {
                            jokers = jokerEntities;
                        }
                    }
                });
    }

    public void clearData() {
        if (jokers != null) {

            jokers.clear();
        }
    }


}
