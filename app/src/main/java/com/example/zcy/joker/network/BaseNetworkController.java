package com.example.zcy.joker.network;

import com.example.zcy.joker.JokerApplication;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jude.utils.JUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Func1;

/**
 * Created by zhangcaoyang on 16/5/19.
 */
public class BaseNetworkController {

    public static final String BASE_URL = "http://japi.juhe.cn/";

    public Retrofit retrofit;
    public Retrofit retrofitCache;
    private final int DEFAULT_TIMEOUT = 5;
    private Retrofit.Builder retrofitBuild;

    public BaseNetworkController() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        retrofitBuild = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        setRetrofit(gson);
        setRetrofitCache(gson);
    }

    /**
     * 设置没有缓存的retrofit
     */
    private void setRetrofit(Gson gson) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    /**
     * 设置带有缓存的retrofit
     */
    private void setRetrofitCache(Gson gson) {
        retrofitCache = new Retrofit.Builder()
                .client(getHttpClientBuilderCache().build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public OkHttpClient.Builder getHttpClientBuilderCache() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!JUtils.isNetWorkAvilable()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);

                if (JUtils.isNetWorkAvilable()) {
                    int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
                    JUtils.Log("has network maxAge=" + maxAge);
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    JUtils.Log("network error");
                    int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                    JUtils.Log("has maxStale=" + maxStale);
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                    JUtils.Log("response build maxStale=" + maxStale);
                }
                return response;
            }
        };

        //设置缓存路径
        File httpCacheDirectory = new File(JokerApplication.mContext.getCacheDir(), "responses");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        httpClientBuilder.addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor).cache(cache);

        return httpClientBuilder;
    }

    /**
     * Rxjava
     * 定义一个转换将返回的数据转换为需要的数据
     */
    protected class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.error_code != 0) {
                try {
                    throw new ApiException(httpResult.error_code, httpResult.reason);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            return httpResult.result.data;
        }
    }

    /**
     * 自定义异常处理
     * 网络返回不为0的时候抛次异常
     */
    protected class ApiException extends Throwable {
        public ApiException(int error_code, String reason) {
            super(error_code + reason);

        }
    }

}
