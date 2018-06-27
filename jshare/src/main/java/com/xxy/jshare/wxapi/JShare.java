package com.xxy.jshare.wxapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.utils.Logger;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatFavorite;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jiguang.share.weibo.SinaWeiboMessage;

import static com.weex.app.weexmodule.JShareModule.EMOJI_FLAG;
import static com.weex.app.weexmodule.JShareModule.FILE_FLAG;
import static com.weex.app.weexmodule.JShareModule.IMG_LOC_FLAG;
import static com.weex.app.weexmodule.JShareModule.IMG_URL_FLAG;
import static com.weex.app.weexmodule.JShareModule.MUSIC_FLAG;
import static com.weex.app.weexmodule.JShareModule.TEXT_FLAG;
import static com.weex.app.weexmodule.JShareModule.VIDEO_FLAG;
import static com.weex.app.weexmodule.JShareModule.WEBPAGE_FLAG;

/**
 * Created by QYG_XXY on 0014 2018/3/14.
 */

public class JShare {
    private static final String TAG = "JShare";
    private ShareBoard mShareBoard;

    /**
     * .setWechat("wxc40e16f3ba6ebabc", "dcad950cd0633a27e353477c4ec12e7a")
     * .setQQ("1106011004", "YIbPvONmBQBZUGaN")
     * .setSinaWeibo("374535501", "baccd12c166f1df96736b51ffbf600a2", "https://www.jiguang.cn");
     *
     * @param context
     * @param weChatAppId
     * @param weChatSecret
     * @param QQAppId
     * @param QQAppKey
     * @param sinaAppKey
     * @param sinaAppSecret
     * @param sinaRedirectUrl
     */
    public static void init(Context context,
                            String weChatAppId,
                            String weChatSecret,
                            String QQAppId,
                            String QQAppKey,
                            String sinaAppKey,
                            String sinaAppSecret,
                            String sinaRedirectUrl) {
        PlatformConfig platformConfig = new PlatformConfig()
                .setWechat(weChatAppId, weChatSecret)
                .setQQ(QQAppId, QQAppKey)
                .setSinaWeibo(sinaAppKey, sinaAppSecret, sinaRedirectUrl);
//                .setFacebook("1847959632183996", "JShareDemo")
//                .setTwitter("eRJyErWUhRZVqBzADAbUnNWx5", "Oo7DJMiBwBHGFWglFrML1ULZCUDlH990RlJlQDdfepm3lToiMC");
        /**
         * since 1.5.0，1.5.0版本后增加API，支持在代码中设置第三方appKey等信息，当PlatformConfig为null时，或者使用JShareInterface.init(Context)时需要配置assets目录下的JGShareSDK.xml
         **/
        JShareInterface.init(context, platformConfig);
//        JShareInterface.init(this);
    }

    public void showBroadView(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("请稍候");
        if (mShareBoard == null) {
            mShareBoard = new ShareBoard(activity);
            List<String> platforms = JShareInterface.getPlatformList();
            if (platforms != null) {
                Iterator var2 = platforms.iterator();
                while (var2.hasNext()) {
                    String temp = (String) var2.next();
                    SnsPlatform snsPlatform = createSnsPlatform(temp);
                    mShareBoard.addPlatform(snsPlatform);
                }
            }
            mShareBoard.setShareboardclickCallback(mShareBoardlistener);
        }
        mShareBoard.show();
    }

    private int mAction = Platform.ACTION_SHARE;
    private ProgressDialog progressDialog;
    private ShareBoardlistener mShareBoardlistener = new ShareBoardlistener() {
        @Override
        public void onclick(SnsPlatform snsPlatform, String platform) {

            switch (mAction) {
                case Platform.ACTION_SHARE:
                    progressDialog.show();
                    //这里以分享链接为例
                    ShareParams shareParams = new ShareParams();
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    shareParams.setTitle("title");
                    shareParams.setText("share_text");
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    shareParams.setUrl("share_url");
                    shareParams.setImagePath("ImagePath");
                    JShareInterface.share(platform, shareParams, mShareListener);
                    break;
                default:
                    break;
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String toastMsg = (String) msg.obj;
            Toast.makeText(progressDialog.getContext(), toastMsg, Toast.LENGTH_SHORT).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    private PlatActionListener mShareListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享成功";
                handler.sendMessage(message);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            Logger.e(TAG, "error:" + errorCode + ",msg:" + error);
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享失败:" + error.getMessage() + "---" + errorCode;
                handler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.obj = "分享取消";
                handler.sendMessage(message);
            }
        }
    };

    public static SnsPlatform createSnsPlatform(String platformName) {
        String mShowWord = platformName;
        String mIcon = "";
        String mGrayIcon = "";
        String mKeyword = platformName;
        int mFlag = 0;
        if (Wechat.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_wechat";
            mGrayIcon = "jiguang_socialize_wechat";
            mShowWord = "jiguang_socialize_text_weixin_key";
            mFlag = TEXT_FLAG | IMG_LOC_FLAG | WEBPAGE_FLAG | MUSIC_FLAG | VIDEO_FLAG | EMOJI_FLAG | FILE_FLAG;
        } else if (WechatMoments.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_wxcircle";
            mGrayIcon = "jiguang_socialize_wxcircle";
            mShowWord = "jiguang_socialize_text_weixin_circle_key";
            mFlag = TEXT_FLAG | IMG_LOC_FLAG | WEBPAGE_FLAG | MUSIC_FLAG | VIDEO_FLAG;
        } else if (WechatFavorite.Name.equals(platformName)) {
            mIcon = "jiguang_socialize_wxfavorite";
            mGrayIcon = "jiguang_socialize_wxfavorite";
            mShowWord = "jiguang_socialize_text_weixin_favorite_key";
            mFlag = TEXT_FLAG | IMG_LOC_FLAG | WEBPAGE_FLAG | MUSIC_FLAG | VIDEO_FLAG;
        }
//        else if (SinaWeibo.Name.equals(platformName)) {
//            mIcon = "jiguang_socialize_sina";
//            mGrayIcon = "jiguang_socialize_sina";
//            mShowWord = "jiguang_socialize_text_sina_key";
//            mFlag = TEXT_FLAG | IMG_LOC_FLAG | WEBPAGE_FLAG | MUSIC_FLAG | VIDEO_FLAG;
//        } else if (SinaWeiboMessage.Name.equals(platformName)) {
//            mIcon = "jiguang_socialize_sina";
//            mGrayIcon = "jiguang_socialize_sina";
//            mShowWord = "jiguang_socialize_text_sina_msg_key";
//            mFlag = WEBPAGE_FLAG;
//        } else if (QQ.Name.equals(platformName)) {
//            mIcon = "jiguang_socialize_qq";
//            mGrayIcon = "jiguang_socialize_qq";
//            mShowWord = "jiguang_socialize_text_qq_key";
//            mFlag = IMG_LOC_FLAG | WEBPAGE_FLAG | MUSIC_FLAG | VIDEO_FLAG;
//        } else if (QZone.Name.equals(platformName)) {
//            mIcon = "jiguang_socialize_qzone";
//            mGrayIcon = "jiguang_socialize_qzone";
//            mShowWord = "jiguang_socialize_text_qq_zone_key";
//            mFlag = TEXT_FLAG | IMG_LOC_FLAG | IMG_URL_FLAG | WEBPAGE_FLAG | MUSIC_FLAG | VIDEO_FLAG;
//        }

//        else if (Facebook.Name.equals(platformName)) {
//            mIcon = "jiguang_socialize_facebook";
//            mGrayIcon = "jiguang_socialize_facebook";
//            mShowWord = "jiguang_socialize_text_facebook_key";
//        } else if (FbMessenger.Name.equals(platformName)) {
//            mIcon = "jiguang_socialize_messenger";
//            mGrayIcon = "jiguang_socialize_messenger";
//            mShowWord = "jiguang_socialize_text_messenger_key";
//        }else if (Twitter.Name.equals(platformName)) {
//            mIcon = "jiguang_socialize_twitter";
//            mGrayIcon = "jiguang_socialize_twitter";
//            mShowWord = "jiguang_socialize_text_twitter_key";
//        }
        return ShareBoard.createSnsPlatform(mShowWord, mKeyword, mIcon, mGrayIcon, 0, mFlag);
    }
}
