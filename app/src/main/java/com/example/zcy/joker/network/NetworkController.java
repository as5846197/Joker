package com.example.zcy.joker.network;


import com.example.zcy.joker.jokers.data.JokerEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangcaoyang on 16/5/17.
 */
public class NetworkController extends BaseNetworkController {
    public static NetworkController instance;

    public static NetworkController getInstance() {
        if (instance == null) {
            instance = new NetworkController();
        }
        return instance;
    }


    public Observable<List<JokerEntity>> getJokerList(int page, String pageSize) {
        JokerInterface service = retrofitCache.create(JokerInterface.class);
        Map<String, String> options = new HashMap<>();
        options.put("key", "ecbed56a3db017e6033a203ba4d347d0");
        options.put("page", page + "");
        options.put("pagesize", pageSize);
        return service.getJokerList(options).map(new HttpResultFunc<List<JokerEntity>>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
