package com.weex.app.weexmodule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.base.library.UserBean;
import com.google.gson.Gson;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.xxy.jshare.wxapi.BaseCallBackBean;
import com.xxy.jshare.wxapi.JShare;
import com.xxy.jshare.wxapi.Logger;
import com.xxy.jshare.wxapi.ShareBoard;
import com.xxy.jshare.wxapi.ShareBoardlistener;
import com.xxy.jshare.wxapi.SnsPlatform;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.weibo.SinaWeibo;

/**
 * Created by QYG_XXY on 0004 2018/4/4.
 */

public class JShareModule extends WXModule {
    /**
     * 文字分享标志
     */
    public static final int TEXT_FLAG = 1;
    /**
     * 本地图片分享标志
     */
    public static final int IMG_LOC_FLAG = 1 << 1;
    /**
     * 图片url分享标志
     */
    public static final int IMG_URL_FLAG = 1 << 2;
    /**
     * 网页链接分享标志
     */
    public static final int WEBPAGE_FLAG = 1 << 3;
    /**
     * 音乐分享标志
     */
    public static final int MUSIC_FLAG = 1 << 4;
    /**
     * 视频分享标志
     */
    public static final int VIDEO_FLAG = 1 << 5;
    /**
     * emoji表情分享标志
     */
    public static final int EMOJI_FLAG = 1 << 6;
    /**
     * 文件分享标志
     */
    public static final int FILE_FLAG = 1 << 7;

    public static final int ALL_FLAG = TEXT_FLAG | IMG_LOC_FLAG | IMG_URL_FLAG | WEBPAGE_FLAG | MUSIC_FLAG | VIDEO_FLAG | EMOJI_FLAG | FILE_FLAG;

    private static final String TAG = "JShareModule";
    private ProgressDialog progressDialog;
    private ShareBoard mShareBoard;

    private JSCallback shareJSCallback;

    public void setModule(WXSDKInstance instance) {
        mWXSDKInstance = instance;
    }

    /**
     * 发起分享
     * <p>
     * flag 参数选择:
     * public static final int TEXT_FLAG = 1;//文字分享标志
     * public static final int IMG_LOC_FLAG = 1 << 1;//本地图片分享标志
     * public static final int IMG_URL_FLAG = 1 << 2;// 图片url分享标志
     * public static final int WEBPAGE_FLAG = 1 << 3;//网页链接分享标志
     * public static final int MUSIC_FLAG = 1 << 4;//音乐分享标志
     * public static final int VIDEO_FLAG = 1 << 5;//视频分享标志
     * public static final int EMOJI_FLAG = 1 << 6;//emoji表情分享标志
     * public static final int FILE_FLAG = 1 << 7;//文件分享标志
     * <p>
     * json参数简要说明():
     * public String title;//标题
     * public String text;//文字内容
     * public String[] imagePath;// 本地图片地址数组，大部分分享只支持一张图片，则取第一个
     * public String[] imageUrl;//图片url数组，大部分分享只支持分享一张图片，则取第一个图片地址
     * public String url;//只用于分享链接，网页
     * public String musicUrl;//分享音乐的url
     * public String shareUrl;//只用于分享音乐,具体未测试
     * public String videoPath;//本地视频路径 只有QQ空间、Facebook、twitter支持本地视频
     * public String videoUrl;//视频url
     * public String filePath;//本地视频路径
     *
     * @param flag       分享类型 参考以上flag
     * @param shareJson  极光share参数拼装复杂，故使用json传参数,简要参考以上，具体参数规则参考http://docs.jiguang.cn/jshare/client/Android/android_api/#qq
     *                   所有参数如:{"filePath":"filePath","imagePath":["/sdcard/123.png","/sdcard/1.png"],"imageUrl":["http://SDFD/SDFSD.PNG","https://5646/455.PNG"],"musicUrl":"/sdcard/123.mp3","shareUrl":"http://6456546/15","text":"文字内容","title":"标题","url":"http://www.baidu.com","videoPath":"/sdcard/45.mp4","videoUrl":"http://sdfsdf.mp4"}
     * @param jsCallback
     */
//    @JSMethod(uiThread = true)
    public void share(final int flag, String shareJson, JSCallback jsCallback) {
        BaseCallBackBean<UserBean> beanBaseCallBackBean = new BaseCallBackBean<>();
        ShareJSONBean shareJSONBean = null;
        try {
            shareJSONBean = new Gson().fromJson(shareJson, ShareJSONBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (shareJSONBean == null) {
            jsCallback.invoke(beanBaseCallBackBean.setCode(-200).setMessage("UserModule ::: params to bean error !!!"));
            return;
        }
        final ShareJSONBean paramBean = shareJSONBean;
        if (mWXSDKInstance == null) {
            jsCallback.invoke(beanBaseCallBackBean.setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(beanBaseCallBackBean.setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        Activity activity = null;
        try {
            activity = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (activity == null) {
            jsCallback.invoke(beanBaseCallBackBean.setCode(-200).setMessage("UserModule ::: getInstall - Activity is null!!!"));
            return;
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("请稍候");

        if (mShareBoard == null) {
            mShareBoard = new ShareBoard(activity);
            List<String> platforms = JShareInterface.getPlatformList();//获取可用配置
            if (platforms != null) {
                Iterator var2 = platforms.iterator();
                while (var2.hasNext()) {
                    String temp = (String) var2.next();
                    SnsPlatform snsPlatform = JShare.createSnsPlatform(temp);
                    if (flag == (snsPlatform.mFlag & flag))
                        mShareBoard.addPlatform(snsPlatform);
                }
            }
            mShareBoard.setShareboardclickCallback(new ShareBoardlistener() {
                @Override
                public void onclick(SnsPlatform snsPlatform, String platName) {
                    ShareParams shareParams = new ShareParams();
                    switch (flag) {
                        case TEXT_FLAG://
                            shareParams.setTitle(paramBean.title);
                            shareParams.setText(paramBean.text);
                            shareParams.setShareType(Platform.SHARE_TEXT);
                            break;
                        case IMG_LOC_FLAG://
//                            shareParams.setUrl(share_url);
                            shareParams.setShareType(Platform.SHARE_IMAGE);
                            //twitter支持单张、多张（最多4张本地图片）
//                            if(Twitter.Name.equals(platName)){
//                                String[] array = new String[]{ MyApplication.ImagePath, MyApplication.ImagePath};
//                                shareParams.setImageArray(array);
//                                //shareParams.setImagePath(MyApplication.ImagePath);
//                            }else{
                            if (paramBean.imagePath != null && paramBean.imagePath.length > 0)
                                shareParams.setImagePath(paramBean.imagePath[0]);
//                            }
                            break;
                        case IMG_URL_FLAG://
                            shareParams.setShareType(Platform.SHARE_IMAGE);
                            //QQ空间支持多张图片，超出9张后，会变成上传相册，上传相册时只支持本地图片
                            if (platName.equals(QZone.Name)) {
//                                String[] array = new String[]{share_imageurl, share_imageurl_1};
                                if (paramBean.imageUrl != null && paramBean.imageUrl.length > 0)
                                    shareParams.setImageArray(paramBean.imageUrl);
                            } else {
                                if (paramBean.imageUrl != null && paramBean.imageUrl.length > 0)
                                    shareParams.setImageUrl(paramBean.imageUrl[0]);
                            }
                            break;
                        case WEBPAGE_FLAG://
                            shareParams.setTitle(paramBean.title);
                            shareParams.setText(paramBean.text);
                            shareParams.setShareType(Platform.SHARE_WEBPAGE);
                            shareParams.setUrl(paramBean.url);
                            if (paramBean.imagePath != null && paramBean.imagePath.length > 0)
                                shareParams.setImagePath(paramBean.imagePath[0]);
                            break;
                        case MUSIC_FLAG://
                            shareParams.setTitle(paramBean.title);
                            shareParams.setText(paramBean.text);
                            shareParams.setShareType(Platform.SHARE_MUSIC);
                            if (platName.equals(SinaWeibo.Name)) {
                                shareParams.setUrl(paramBean.musicUrl);
                            } else {
                                shareParams.setMusicUrl(paramBean.musicUrl);
                                shareParams.setUrl(paramBean.shareUrl);
                                if (paramBean.imagePath != null && paramBean.imagePath.length > 0)
                                    shareParams.setImagePath(paramBean.imagePath[0]);
                            }
                            break;
                        case VIDEO_FLAG://
                            //QQ空间、Facebook、twitter支持本地视频
                            /**
                             * twitter的视频的格式要求较多，具体参考twitter文档：https://developer.twitter.com/en/docs/media/upload-media/uploading-media/media-best-practices
                             * Video files must meet all of the following criteria:
                             * Duration should be between 0.5 seconds and 30 seconds (sync) / 140 seconds (async)
                             * File size should not exceed 15 mb (sync) / 512 mb (async)
                             * Dimensions should be between 32x32 and 1280x1024
                             * Aspect ratio should be between 1:3 and 3:1
                             * Frame rate should be 40fps or less
                             * Must not have open GOP
                             * Must use progressive scan
                             * Must have 1:1 pixel aspect ratio
                             * Only YUV 4:2:0 pixel format is supported.
                             * Audio should be mono or stereo, not 5.1 or greater
                             * Audio must be AAC with Low Complexity profile. High-Efficiency AAC is not supported.
                             */
                            shareParams.setShareType(Platform.SHARE_VIDEO);
                            if (platName.equals(QZone.Name)
//                                    || platName.equals(Facebook.Name) || platName.equals(FbMessenger.Name) || platName.equals(Twitter.Name)
                                    ) {
                                shareParams.setVideoPath(paramBean.videoPath);
                            } else {
                                shareParams.setTitle(paramBean.title);
                                shareParams.setText(paramBean.text);
                                shareParams.setUrl(paramBean.videoUrl);
                                if (paramBean.imagePath != null && paramBean.imagePath.length > 0)
                                    shareParams.setImagePath(paramBean.imagePath[0]);
                            }
                            break;
                        case EMOJI_FLAG://
                            //只有微信支持表情
                            shareParams.setShareType(Platform.SHARE_EMOJI);
                            if (paramBean.imagePath != null && paramBean.imagePath.length > 0)
                                shareParams.setImagePath(paramBean.imagePath[0]);
                            break;
                        case FILE_FLAG://
                            //只有微信支持文件
                            shareParams.setShareType(Platform.SHARE_FILE);
                            shareParams.setFilePath(paramBean.filePath);
                            break;
                    }
                    JShareInterface.share(platName, shareParams, mShareListener);
                }
            });
        }
        mShareBoard.show();
    }

    public static class ShareJSONBean {
        /**
         * 标题
         */
        public String title;
        /**
         * 文字内容
         */
        public String text;
        /**
         * 本地图片地址数组，大部分分享只支持一张图片，则取第一个
         */
        public String[] imagePath;
        /**
         * 图片url数组，大部分分享只支持分享一张图片，则取第一个图片地址
         */
        public String[] imageUrl;
        /**
         * 只用于分享链接，网页
         */
        public String url;
        /**
         * 分享音乐的url
         */
        public String musicUrl;
        /**
         * 只用于分享音乐,具体未测试
         */
        public String shareUrl;
        /**
         * 本地视频路径 只有QQ空间、Facebook、twitter支持本地视频
         */
        public String videoPath;
        /**
         * 视频url
         */
        public String videoUrl;
        /**
         * 本地视频路径
         */
        public String filePath;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String[] getImagePath() {
            return imagePath;
        }

        public void setImagePath(String[] imagePath) {
            this.imagePath = imagePath;
        }

        public String[] getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String[] imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMusicUrl() {
            return musicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
        }

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
        }

        public String getVideoPath() {
            return videoPath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                String toastMsg = (String) msg.obj;
                Toast.makeText(progressDialog.getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                message.obj = "分享失败:[" + errorCode + "] " + error.getMessage();
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
}
