package com.xxy.jshare.wxapi;

/**
 * Created by QYG_XXY on 0012 2018/2/12.
 */

public class BaseCallBackBean<T> {
    public int code = 0;
    public String message = "success";
    public T data;

    public BaseCallBackBean<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public BaseCallBackBean<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public BaseCallBackBean<T> setData(T data) {
        this.data = data;
        return this;
    }
}
