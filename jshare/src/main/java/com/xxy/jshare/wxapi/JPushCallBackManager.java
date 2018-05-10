package com.xxy.jshare.wxapi;

import android.text.TextUtils;

import com.taobao.weex.bridge.JSCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by QYG_XXY on 0010 2018/5/10.
 */

public class JPushCallBackManager {
    private Map<String, JSCallback> callbackMap;
    private static JPushCallBackManager manager;

    private JPushCallBackManager() {
        callbackMap = new HashMap<>();
    }

    public static JPushCallBackManager getInstance() {
        if (manager == null) {
            synchronized (JPushCallBackManager.class) {
                if (manager == null) {
                    manager = new JPushCallBackManager();
                }
            }
        }
        return manager;
    }

    public void putCallBack(int sequence, JSCallback callback) {
        if (callback == null) return;
        callbackMap.put(sequence + "", callback);
    }

    public void putCallBack(String sequence, JSCallback callback) {
        if (callback == null) return;
        if (TextUtils.isEmpty(sequence)) return;
        callbackMap.put(sequence, callback);
    }

    public JSCallback getCallBack(int sequence) {
        JSCallback callback = callbackMap.get(sequence + "");
        callbackMap.remove(sequence + "");
        return callback;
    }

    public JSCallback getCallBack(String sequence) {
        if (TextUtils.isEmpty(sequence)) return null;
        JSCallback callback = callbackMap.get(sequence);
        callbackMap.remove(sequence);
        return callback;
    }
}
