package com.example.zcy.joker.network;

import com.example.zcy.joker.jokers.data.JokerEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zhangcaoyang on 16/5/16.
 */
public interface JokerInterface {
    @GET("joke/content/text.from")
    Observable<HttpResult<List<JokerEntity>>> getJokerList(@QueryMap Map<String, String> options);

}
