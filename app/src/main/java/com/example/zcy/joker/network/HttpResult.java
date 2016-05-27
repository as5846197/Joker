package com.example.zcy.joker.network;

/**
 * Created by zhangcaoyang on 16/5/18.
 */
public class HttpResult<T> {
    public int error_code;
    public String reason;
    public Result<T> result;

    class Result<T> {
        public T data;

    }

}
