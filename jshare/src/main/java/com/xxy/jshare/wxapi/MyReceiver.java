package com.xxy.jshare.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.base.library.LogX;
import com.base.library.MyWeexManager;
import com.taobao.weex.WXSDKInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jiguang.share.android.utils.Logger;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private static final String NEW_PUSH_KEY = "newJGuangPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogX.e(TAG, "[MyReceiver] Action： " + intent.getAction() + "\nextras:\n" + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {//设备识别号监听，可以保存到服务器用于点对点推送
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...
                Map<String, Object> map = new HashMap<>();
                map.put("regId", regId);
                map.put("action", "registrationID");
                sendMsg(map);
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);
                String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);//唯一标识消息的 ID, 可用于上报统计等

                String title = bundle.getString(JPushInterface.EXTRA_TITLE);//保存服务器推送下来的消息的标题。
                // 对应 API 消息内容的 title 字段。
                // Portal 推送消息界上不作展示

                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);//保存服务器推送下来的消息内容。
                //对应 API 消息内容的 message 字段。
                //对应 Portal 推送消息界面上的"自定义消息内容”字段

                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                //保存服务器推送下来的附加字段。这是个 JSON 字符串。
                //对应 API 消息内容的 extras 字段。
                //对应 Portal 推送消息界面上的“可选设置”里的附加字段。
                Map<String, Object> map = new HashMap<>();
                JSONObject extraJson = null;
                if (!ExampleUtil.isEmpty(extras)) {
                    try {
                        extraJson = new JSONObject(extras);
                        if (extraJson.length() > 0) {

                        }
                    } catch (JSONException e) {
                    }
                }
                map.put("action", "messageReceived");
                map.put("msgId", msgId);
                map.put("title", title);
                map.put("message", message);
                map.put("extras", extraJson);
                sendMsg(map);
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//通知栏的Notification ID，可以用于清除Notification
                Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//对应 API 通知内容的 title 字段。
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);//保存服务器推送下来的通知内容。
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);//保存服务器推送下来的附加字段。这是个 JSON 字符串。
                String fileHtml = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH);//富媒体通知推送下载的HTML的文件路径,用于展现WebView。
                String fileStr = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES);
                
				String[] fileNames =fileStr==null?null: fileStr.split(",");//富媒体通知推送下载的图片资源的文件名,多个文件名用 “，” 分开。 与 “JPushInterface.EXTRA_RICHPUSH_HTML_PATH” 位于同一个路径。
                String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);//唯一标识通知消息的 ID, 可用于上报统计等。
                String bigText = bundle.getString(JPushInterface.EXTRA_BIG_TEXT);//大文本通知样式中大文本的内容。
                String bigPicPath = bundle.getString(JPushInterface.EXTRA_BIG_PIC_PATH);//可支持本地图片的路径，或者填网络图片地址。大图片通知样式中大图片的路径/地址。
                String inboxJson = bundle.getString(JPushInterface.EXTRA_INBOX);//获取的是一个 JSONObject，json 的每个 key 对应的 value 会被当作文本条目逐条展示。收件箱通知样式中收件箱的内容。
                String prio = bundle.getString(JPushInterface.EXTRA_NOTI_PRIORITY);//通知的优先级。 默认为0，范围为 -2～2 ，其他值将会被忽略而采用默认。
                String categry = bundle.getString(JPushInterface.EXTRA_NOTI_CATEGORY);//通知分类。完全依赖 rom 厂商对每个 category 的处理策略，比如通知栏的排序

                Map<String, Object> map = new HashMap<>();
                map.put("action", "notificationReceived");
                map.put("notifactionId", notifactionId); //通知栏的Notification ID，可以用于清除Notification
                map.put("title", title); //对应 API 通知内容的 title 字段。
                map.put("content", content); //保存服务器推送下来的通知内容。
                map.put("extras", extras); //保存服务器推送下来的附加字段。这是个 JSON 字符串。
                map.put("fileHtml", fileHtml); //富媒体通知推送下载的HTML的文件路径,用于展现WebView。
                map.put("imgStr", fileStr);
                map.put("imgs", fileNames);
                map.put("msgId", file); //唯一标识通知消息的 ID, 可用于上报统计等。
                map.put("bigText", bigText); //大文本通知样式中大文本的内容。
                map.put("bigPicPath", bigPicPath); //可支持本地图片的路径，或者填网络图片地址。大图片通知样式中大图片的路径/地址。
                map.put("inboxJson", inboxJson); //获取的是一个 JSONObject，json 的每个 key 对应的 value 会被当作文本条目逐条展示。收件箱通知样式中收件箱的内容。
                map.put("prio", prio); //通知的优先级。 默认为0，范围为 -2～2 ，其他值将会被忽略而采用默认。
                map.put("categry", categry); //通知分类。完全依赖 rom 厂商对每个 category 的处理策略，比如通知栏的排序
                sendMsg(map);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                //打开自定义的Activity
                Intent i = new Intent("com.qyq.weex.app.main");
                i.putExtras(bundle);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);//对应 Portal 推送通知界面上的“通知标题”字段。对应 API 通知内容的 title 字段。
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);//对应 Portal 推送通知界面上的“通知内容”字段。对应 API 通知内容的alert字段
                String type = bundle.getString(JPushInterface.EXTRA_EXTRA);//对应 Portal 推送消息界面上的“可选设置”里的附加字段。
                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//通知栏的Notification ID，可以用于清除Notification
                String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);//唯一标识调整消息的 ID, 可用于上报统计等。

                Map<String, Object> map = new HashMap<>();
                map.put("action", "notificationOpened");
                map.put("title", title);
                map.put("content", content);
                map.put("extra", type);
                map.put("notificationId", notificationId);
                map.put("msgId", file);
                sendMsg(map);
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {//JPush 服务的连接状态发生变化。（注：不是指 Android 系统的网络连接状态。）
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);//获取当前 JPush 服务的连接状态。
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
                Map<String, Object> map = new HashMap<>();
                map.put("action", "connectionChange");
                map.put("connected", connected);
                sendMsg(map);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void sendMsg(Map<String, Object> params) {
        if (params == null || params.size() <= 0) return;
        Map<String, WXSDKInstance> wxsdkInstances = MyWeexManager.getInstance().getWXSDKInstances();
        for (Map.Entry<String, WXSDKInstance> entry : wxsdkInstances.entrySet()) {
            //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            WXSDKInstance instance = entry.getValue();
            if (instance != null) {
                instance.fireGlobalEventCallback(NEW_PUSH_KEY, params);
            }
        }
    }

//    /**
//     * 自定义消息解析
//     *
//     * @param context
//     * @param bundle
//     */
//    private void processCustomMessage(Context context, Bundle bundle) {
//        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);//唯一标识消息的 ID, 可用于上报统计等
//
//        String title = bundle.getString(JPushInterface.EXTRA_TITLE);//保存服务器推送下来的消息的标题。
//        // 对应 API 消息内容的 title 字段。
//        // Portal 推送消息界上不作展示
//
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);//保存服务器推送下来的消息内容。
//        //对应 API 消息内容的 message 字段。
//        //对应 Portal 推送消息界面上的"自定义消息内容”字段
//
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        //保存服务器推送下来的附加字段。这是个 JSON 字符串。
//        //对应 API 消息内容的 extras 字段。
//        //对应 Portal 推送消息界面上的“可选设置”里的附加字段。
//
//        if (!ExampleUtil.isEmpty(extras)) {
//            try {
//                JSONObject extraJson = new JSONObject(extras);
//                if (extraJson.length() > 0) {
//
//                }
//            } catch (JSONException e) {
//            }
//        }
//
//    }
}
