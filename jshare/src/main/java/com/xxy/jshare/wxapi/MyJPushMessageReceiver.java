package com.xxy.jshare.wxapi;

import android.content.Context;

import com.taobao.weex.bridge.JSCallback;

import java.util.Map;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 自定义JPush message 接收器,包括操作tag/alias的结果返回(仅仅包含tag/alias新接口部分)
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {
    /**
     * tag增删查改的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Map<String, Object> map = TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
        JSCallback callback = JPushCallBackManager.getInstance().getCallBack(jPushMessage.getSequence());
        if (callback != null) {
            BaseCallBackBean<Map<String, Object>> baseCallBackBean = new BaseCallBackBean<>();
            callback.invokeAndKeepAlive(baseCallBackBean.setData(map));
        }
    }

    /**
     * 查询某个tag与当前用户的绑定状态的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        Map<String, Object> map = TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
        JSCallback callback = JPushCallBackManager.getInstance().getCallBack(jPushMessage.getSequence());
        if (callback != null) {
            BaseCallBackBean<Map<String, Object>> baseCallBackBean = new BaseCallBackBean<>();
            callback.invokeAndKeepAlive(baseCallBackBean.setData(map));
        }
    }

    /**
     * alias相关的操作会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Map<String, Object> map = TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
        JSCallback callback = JPushCallBackManager.getInstance().getCallBack(jPushMessage.getSequence());
        if (callback != null) {
            BaseCallBackBean<Map<String, Object>> baseCallBackBean = new BaseCallBackBean<>();
            callback.invokeAndKeepAlive(baseCallBackBean.setData(map));
        }
    }

    /**
     * 设置手机号码会在此方法中回调结果。
     *
     * @param context
     * @param jPushMessage
     */
    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Map<String, Object> map = TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
        JSCallback callback = JPushCallBackManager.getInstance().getCallBack(jPushMessage.getSequence());
        if (callback != null) {
            BaseCallBackBean<Map<String, Object>> baseCallBackBean = new BaseCallBackBean<>();
            callback.invokeAndKeepAlive(baseCallBackBean.setData(map));
        }
    }
}
