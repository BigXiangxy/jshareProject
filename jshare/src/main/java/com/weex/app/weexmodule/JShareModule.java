package com.weex.app.weexmodule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.google.gson.Gson;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.xxy.jshare.wxapi.BaseCallBackBean;
import com.xxy.jshare.wxapi.JPushCallBackManager;
import com.xxy.jshare.wxapi.JShare;
import com.xxy.jshare.wxapi.Logger;
import com.xxy.jshare.wxapi.ShareBoard;
import com.xxy.jshare.wxapi.ShareBoardlistener;
import com.xxy.jshare.wxapi.SnsPlatform;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.weibo.SinaWeibo;
import cn.jpush.android.api.JPushInterface;

/**
 * 所有带sequence参数的接口 都必须携带此参数且不可重复，否则有可能收不到回调。可以取时间戳
 * Created by QYG_XXY on 0004 2018/4/4.
 */

@WeexModule(name = "jShareLib")
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

    /**
     * 发起分享
     * <p>
     * flag 参数选择:
     * public static final int TEXT_FLAG = 1;//文字分享标志
     * public static final int IMG_LOC_FLAG =2;//本地图片分享标志
     * public static final int IMG_URL_FLAG = 4;// 图片url分享标志
     * public static final int WEBPAGE_FLAG = 8;//网页链接分享标志
     * public static final int MUSIC_FLAG = 16;//音乐分享标志
     * public static final int VIDEO_FLAG = 32;//视频分享标志
     * public static final int EMOJI_FLAG = 64;//emoji表情分享标志
     * public static final int FILE_FLAG = 128;//文件分享标志
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
    @JSMethod(uiThread = true)
    public void share(final int flag, String shareJson, JSCallback jsCallback) {
        BaseCallBackBean<String> beanBaseCallBackBean = new BaseCallBackBean<>();
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
        shareJSCallback = jsCallback;
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
                if (progressDialog != null && progressDialog.isShowing()) {
                    Toast.makeText(mWXSDKInstance.getContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                if (shareJSCallback != null) {
                    BaseCallBackBean<String> beanBaseCallBackBean = new BaseCallBackBean<>();
                    switch (msg.what) {
                        case 1://成功
                            shareJSCallback.invokeAndKeepAlive(beanBaseCallBackBean.setData(toastMsg));
                            break;
                        case 3://取消
                            shareJSCallback.invokeAndKeepAlive(beanBaseCallBackBean.setData(toastMsg));
                            break;
                        default://失败
                            shareJSCallback.invokeAndKeepAlive(beanBaseCallBackBean.setCode(msg.what).setMessage(toastMsg));
                            break;
                    }
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
                message.what = 1;
                message.obj = "分享成功";
                handler.sendMessage(message);
            }
        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
            Logger.e(TAG, "error:" + errorCode + ",msg:" + error);
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.what = errorCode;
                message.obj = "分享失败:[" + errorCode + "] " + error.getMessage();
                handler.sendMessage(message);
            }
        }

        @Override
        public void onCancel(Platform platform, int action) {
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.what = 3;
                message.obj = "分享取消";
                handler.sendMessage(message);
            }
        }
    };


    /**
     * 停止推送服务。
     * 调用了本 API 后，JPush 推送服务完全被停止。具体表现为：
     * 收不到推送消息
     * 极光推送所有的其他 API 调用都无效,不能通过 JPushInterface.init 恢复，需要调用resumePush恢复。
     */
    @JSMethod(uiThread = true)
    public void stopPush() {
        if (mWXSDKInstance == null) {
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            return;
        }
        JPushInterface.stopPush(context);
    }

    /**
     * 恢复推送服务。
     * 调用了此 API 后，极光推送完全恢复正常工作。
     */
    @JSMethod(uiThread = true)
    public void resumePush() {
        if (mWXSDKInstance == null) {
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            return;
        }
        JPushInterface.resumePush(context);
    }

    /**
     * 用来检查 Push Service 是否已经被停止
     * SDK 1.5.2 以上版本支持。
     *
     * @return
     */
    @JSMethod(uiThread = false)
    public boolean isPushStopped() {
        if (mWXSDKInstance == null) {
            return false;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            return false;
        }
        return JPushInterface.isPushStopped(context);
    }

    /**
     * 调用此 API 来设置别名。
     * 需要理解的是，这个接口是覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param alias    每次调用设置有效的别名，覆盖之前的设置。
     *                 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     *                 限制：alias 命名长度限制为 40 字节。（判断长度需采用UTF-8编码）
     */
    @JSMethod(uiThread = true)
    public void setAlias(int sequence, String alias, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.setAlias(context, sequence, alias);
    }


    /**
     * 调用此 API 来删除别名。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    @JSMethod(uiThread = true)
    public void deleteAlias(int sequence, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.deleteAlias(context, sequence);
    }

    /**
     * 调用此 API 来查询别名。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    @JSMethod(uiThread = true)
    public void getAlias(int sequence, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.getAlias(context, sequence);
    }


    /**
     * 调用此 API 来设置标签。
     * 需要理解的是，这个接口是覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性
     * @param tags     限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
     *                 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
     */
    @JSMethod(uiThread = true)
    public void setTags(int sequence, Set<String> tags, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.setTags(context, sequence, tags);
    }


    /**
     * 调用此 API 来新增标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param tags     每次调用至少新增一个 tag。
     *                 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     *                 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
     *                 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
     */
    @JSMethod(uiThread = true)
    public void addTags(int sequence, Set<String> tags, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.addTags(context, sequence, tags);
    }


    /**
     * 调用此 API 来删除指定标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param tags     每次调用至少删除一个 tag。
     *                 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     *                 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
     *                 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
     */
    @JSMethod(uiThread = true)
    public void deleteTags(int sequence, Set<String> tags, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.deleteTags(context, sequence, tags);
    }

    /**
     * 调用此 API 来清除所有标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    @JSMethod(uiThread = true)
    public void cleanTags(int sequence, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.cleanTags(context, sequence);
    }


    /**
     * 调用此 API 来查询所有标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    @JSMethod(uiThread = true)
    public void getAllTags(int sequence, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.getAllTags(context, sequence);
    }


    /**
     * 调用此 API 来查询指定tag与当前用户绑定的状态。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param tag      tag
     */
    @JSMethod(uiThread = true)
    public void checkTagBindState(int sequence, String tag, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.checkTagBindState(context, sequence, tag);
    }

    /**
     * 调用此API设置手机号码。该接口会控制调用频率，频率为 10s之内最多三次。 用于短信补充功能。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.1.1
     *
     * @param sequence     用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param mobileNumber 电话号码 手机号码。如果传null或空串则为解除号码绑定操作。
     *                     限制：只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。
     */
    @JSMethod(uiThread = true)
    public void setMobileNumber(int sequence, String mobileNumber, JSCallback jsCallback) {
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - mWXSDKInstance is null!!!"));
            return;
        }
        Context context = mWXSDKInstance.getContext();
        if (mWXSDKInstance == null) {
            jsCallback.invoke(new BaseCallBackBean<String>().setCode(-200).setMessage("UserModule ::: getInstall - Context is null!!!"));
            return;
        }
        JPushCallBackManager.getInstance().putCallBack(sequence, jsCallback);
        JPushInterface.setMobileNumber(context, sequence, mobileNumber);
    }
}
