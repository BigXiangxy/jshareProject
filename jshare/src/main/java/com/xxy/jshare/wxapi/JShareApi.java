package com.xxy.jshare.wxapi;

import cn.jiguang.share.android.api.AuthListener;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.ShareParams;

/**
 * 平台名称，值可选
 * Wechat.Name、
 * WechatMoments.Name、
 * WechatFavorite.Name、
 * SinaWeibo.Name、
 * SinaWeiboMessage.Name、
 * QQ.Name、
 * QZone.Name、
 * Facebook.Name、
 * FbMessenger.Name、
 * Twitter.Name
 * <p>
 * <p>
 * <p>
 * Created by QYG_XXY on 0014 2018/3/14.
 */

public class JShareApi {

    /**
     * 判断该平台的分享是否有效。
     *
     * @param name
     * @return
     */
    public static boolean isClientValid(String name) {
        return JShareInterface.isClientValid(name);
    }

    /**
     * 分享接口
     *
     * @param name
     * @param shareParams         分享的配置参数，具体设置请参考各个平台的分享参数说明。
     * @param shareActionListener 回调接口，可为 null，为 null 时则没有回调
     */
    public static void share(String name, ShareParams shareParams, PlatActionListener shareActionListener) {
        JShareInterface.share(name, shareParams, shareActionListener);
    }

    /**
     * 判断某平台是否支持授权
     * SDK 1.2.0 以上版本支持
     *
     * @param name
     * @return
     */
    public static boolean isSupportAuthorize(String name) {
        return JShareInterface.isSupportAuthorize(name);
    }

    /**
     * JShareInterface.authorize(platform, new AuthListener() {
     *
     * @param name
     * @param authListener 回调接口，可为 null，为 null 时则没有回调。
     * @Override public void onComplete(Platform platform, int i, BaseResponseInfo data) {
     * Logger.dd(TAG, "onComplete:" + platform + ",action:" + action + ",data:" + data);
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_AUTHORIZING:
     * if (data instanceof AccessTokenInfo) {        //授权信息
     * String token = ((AccessTokenInfo) data).getToken();// token
     * long expiration = ((AccessTokenInfo) data).getExpiresIn();// token有效时间，时间戳
     * String refresh_token = ((AccessTokenInfo) data).getRefeshToken();//refresh_token
     * String openid = ((AccessTokenInfo) data).getOpenid();//openid
     * //授权原始数据，开发者可自行处理
     * String originData = data.getOriginData();
     * toastMsg = "授权成功:" + data.toString();
     * Logger.dd(TAG, "openid:" + openid + ",token:" + token + ",expiration:" + expiration + ",refresh_token:" + refresh_token);
     * Logger.dd(TAG, "originData:" + originData);
     * }
     * break;
     * }
     * }
     * @Override public void onError(Platform platform, int i, int i1, Throwable throwable) {
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_AUTHORIZING:
     * toastMsg = "授权失败";
     * break;
     * }
     * }
     * @Override public void onCancel(Platform platform, int i) {
     * Logger.dd(TAG, "onCancel:" + platform + ",action:" + action);
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_AUTHORIZING:
     * toastMsg = "取消授权";
     * break;
     * }
     * }
     * });
     * 授权接口
     * SDK 1.2.0 以上版本支持
     */
    public static void authorize(String name, AuthListener authListener) {
        JShareInterface.authorize(name, authListener);
    }

    /**
     * 判断是否已经授权接口
     * SDK 1.2.0以上版本支持
     *
     * @param name
     * @return
     */
    public static boolean isAuthorize(String name) {
        return JShareInterface.isAuthorize(name);
    }

    /**
     * 删除授权接口
     * SDK 1.2.0 以上版本支持
     * <p>
     * JShareInterface.removeAuthorize(platform, new AuthListener() {
     *
     * @param name
     * @param actionListener 回调接口，可为 null，为 null 时则没有回调
     * @Override public void onComplete(Platform platform, int i, BaseResponseInfo data) {
     * Logger.dd(TAG, "onComplete:" + platform + ",action:" + action + ",data:" + data);
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_REMOVE_AUTHORIZING:
     * toastMsg = "删除授权成功";
     * break;
     * }
     * }
     * @Override public void onError(Platform platform, int i, int i1, Throwable throwable) {
     * Logger.dd(TAG, "onError:" + platform + ",action:" + action + ",error:" + error);
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_REMOVE_AUTHORIZING:
     * toastMsg = "删除授权失败";
     * break;
     * }
     * }
     * @Override public void onCancel(Platform platform, int i) {
     * <p>
     * }
     * });
     */
    public static void removeAuthorize(String name, AuthListener actionListener) {
        JShareInterface.removeAuthorize(name, actionListener);
    }

    /**
     * 获取个人信息接口
     * SDK 1.2.0 以上版本支持
     * <p>
     * JShareInterface.getUserInfo(platform, new AuthListener() {
     *
     * @param name
     * @param authListener 回调接口，可为 null，为 null 时则没有回调
     * @Override public void onComplete(Platform platform, int i, BaseResponseInfo data) {
     * Logger.dd(TAG, "onComplete:" + platform + ",action:" + action + ",data:" + data);
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_USER_INFO:
     * if (data instanceof UserInfo) {      //第三方个人信息
     * String openid = ((UserInfo) data).getOpenid();  //openid
     * String name = ((UserInfo) data).getName();  //昵称
     * String imageUrl = ((UserInfo) data).getImageUrl();  //头像url
     * int gender = ((UserInfo) data).getGender();//性别, 1表示男性；2表示女性
     * //个人信息原始数据，开发者可自行处理
     * String originData = data.getOriginData();
     * toastMsg = "获取个人信息成功:" + data.toString();
     * Logger.dd(TAG, "openid:" + openid + ",name:" + name + ",gender:" + gender + ",imageUrl:" + imageUrl);
     * Logger.dd(TAG, "originData:" + originData);
     * }
     * break;
     * }
     * }
     * @Override public void onError(Platform platform, int i, int i1, Throwable throwable) {
     * Logger.dd(TAG, "onError:" + platform + ",action:" + action + ",error:" + error);
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_USER_INFO:
     * toastMsg = "获取个人信息失败";
     * break;
     * }
     * }
     * @Override public void onCancel(Platform platform, int i) {
     * Logger.dd(TAG, "onCancel:" + platform + ",action:" + action);
     * String toastMsg = null;
     * switch (action) {
     * case Platform.ACTION_USER_INFO:
     * toastMsg = "取消获取个人信息";
     * break;
     * }
     * }
     * });
     */
    public static void getUserInfo(String name, AuthListener authListener) {
        JShareInterface.getUserInfo(name, authListener);
    }

    /******************************微信开始**************************/
//    微信(包括微信朋友圈、微信收藏)
//    1）分享文本
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_TEXT
//    Text 是	String	分享标题	不超过 10KB
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_TEXT);
//    shareParams.setText("Text");//必须

//    2）分享图片
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_IMAGE
//    ImagePath	否	String	缩略图，本地图片路径	长度不能超过 10KB,大小不能超过 32K,与 ImageData 二选一
//    ImageData	否	Bitmap	缩略图，图片 Bitmap	大小不能超过 32K,与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_IMAGE);
//    shareParams.setImagePath(file.getAbsolutePath());
//    //shareParams.setImageData(bitmap);

//    3）分享音乐
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_MUSIC
//    Title	否	String	音乐标题	长度不能超过 512
//    Text	否	String	音乐描述	长度不能超过 1K
//    MusicUrl	是	String	音乐资源 Url	点击播放按钮可直接播放 url,长度不能超过 10K
//    Url	否	String	跳转 Url	点击跳转页面 url,长度不能超过 10K
//    ImagePath	否	String	缩略图，本地图片路径	长度不能超过 10KB,大小不能超过 32K,与 ImageData 二选一
//    ImageData	否	Bitmap	缩略图，图片 Bitmap	大小不能超过 32K,与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setTitle(share_title);
//    shareParams.setText(share_text);
//    shareParams.setShareType(Platform.SHARE_MUSIC);
//    shareParams.setUrl(url);
//    shareParams.setMusicUrl(music_url);
//    shareParams.setImagePath(file.getAbsolutePath());

//    4）分享视频
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_VIDEO
//    Title	否	String	视频标题	长度不能超过 512
//    Text	否	String	视频描述	长度不能超过 1K，朋友圈不显示该字段内容
//    Url	是	String	视频Url	长度不能超过 10K
//    ImagePath	否	String	缩略图，本地图片路径	长度不能超过 10KB,大小不能超过 32K,与 ImageData 二选一
//    ImageData	否	Bitmap	缩略图，图片 Bitmap	大小不能超过 32K,与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setTitle(share_title);
//    shareParams.setText(share_text);
//    shareParams.setShareType(Platform.SHARE_VIDEO);
//    shareParams.setUrl(share_videourl);
//    shareParams.setImagePath(file.getAbsolutePath());

//    5）分享网页
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_WEBPAGE
//    Title	否	String	网页标题	长度不能超过 512
//    Text 否	String	网页描述	长度不能超过 1K，朋友圈不显示该字段内容
//    Url	是	String	网页 Url	长度不能超过 10K
//    ImagePath	否	String	缩略图，本地图片路径	长度不能超过 10KB,大小不能超过 32K,与 ImageData 二选一
//    ImageData	否	Bitmap	缩略图，图片 Bitmap	大小不能超过 32K,与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setTitle(share_title);
//    shareParams.setText(share_text);
//    shareParams.setShareType(Platform.SHARE_WEBPAGE);
//    shareParams.setUrl(share_url);//必须
//    shareParams.setImagePath(file.getAbsolutePath());

//    6）分享 Emoji 表情（朋友圈、微信收藏不支持）
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_EMOJI
//    ImagePath	否	String	本地图片路径	长度不能超过 10KB,大小不能超过 10M，ImagePath 与 ImageData 必须二选一
//    ImageData	否	Bitmap	图片 Bitmap	大小不能超过 10M，ImagePath 与 ImageData 必须二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_EMOJI);
//    shareParams.setImagePath(file.getAbsolutePath());

//    7）分享文件（朋友圈、微信收藏不支持）
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_FILE
//    FilePath	是	String	本地文件路径	长度不能超过 10KB,大小不能超过 10M
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_FILE);
//    shareParams.setFilePath(file.getAbsolutePath());

    /******************************微信结束**************************/

    /******************************QQ开始**************************/
//    QQ

//    1）分享图片
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_IMAGE
//    ImagePath	否	String	本地图片路径	ImagePath 与 ImageUrl 必须二选一
//    ImageUrl	否	String	网络图片地址	必须以 http 或 https 开头,ImagePath 与 ImageUrl 必须二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_IMAGE);
//    shareParams.setImagePath(file.getAbsolutePath());

//    2）分享链接
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_WEBPAGE
//    Title	否	String	标题	不超过 30 字符
//    Text	否	String	描述	不超过 40 字符
//    ImagePath	否	String	缩略图，本地图片路径	与 ImageUrl 二选一
//    ImageUrl	否	String	缩略图，网络图片地址	必须以 http 或 https 开头,与 ImagePath 二选一
//    Url	是	String	跳转链接	必须以 http 或 https 开头
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_WEBPAGE);
//    shareParams.setTitle(share_title);
//    shareParams.setText(share_text);
//    shareParams.setUrl(share_url);
//    shareParams.setImagePath(file.getAbsolutePath());

//    3）分享音乐
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_MUSIC
//    Title	否	String	标题	不超过 30 字符
//    Text	否	String	描述	不超过 40 字符
//    MusicUrl	是	String	音乐链接	音乐文件的远程链接 ,点击播放按钮可直接播放， 以 URL 的形式传入 , 不支持本地音乐，必须以 http 或 https 开头
//    Url	是	String	跳转链接	跳转页面 url,必须以 http 或 https 开头
//    ImagePath	否	String	缩略图，本地图片路径	与 ImageUrl 二选一
//    ImageUrl	否	String	缩略图，网络图片地址	必须以 http 或 https 开头,与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_MUSIC);
//    shareParams.setTitle(share_title);
//    shareParams.setText(share_text);
//    shareParams.setUrl(share_url);
//    shareParams.setMusicUrl(music_url);
//    shareParams.setImagePath(file.getAbsolutePath());
//    //shareParams.setImageUrl("http://inews.gtimg.com/newsapp_bt/0/876781763/1000");
    /******************************QQ结束**************************/
    /******************************QQ空间开始**************************/
//    QQ空间

//    1)分享文本
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_TEXT
//    Text	否	String	描述	不超过 10000 字符
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_TEXT);
//    shareParams.setText(share_text);

//    2)分享图片
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_IMAGE
//    ImagePath	否	String	本地图片路径	ImagePath与ImageUrl、ImageArray必须三选一
//    ImageUrl	否	String	网络图片地址	必须以http或https开头,ImagePath与ImageUrl、ImageArray必须三选一
//    ImageArray	否	Array	图片地址数组	支持多个图片，超出9张后，会变成上传相册，上传相册时只支持本地图片,ImagePath与ImageUrl、ImageArray必须三选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_IMAGE);
//    shareParams.setImagePath(MyApplication.ImagePath);
//    //shareParams.setImageUrl(share_imageurl);
//    //String[] array = new String[]{ share_imageurl, share_imageurl_1};
    //shareParams.setImageArray(array);

//    3)分享链接
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_WEBPAGE
//    Title	是	String	标题	最长 200 个字符
//    Text	否	String	描述	最长 600 个字符
//    Url	是	String	跳转链接	必须以 http 或 https 开头
//    ImagePath	否	String	缩略图，本地图片路径	与 ImageUrl 二选一
//    ImageUrl	否	String	缩略图，网络图片地址	必须以 http 或 https 开头,与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_WEBPAGE);
//    shareParams.setTitle(share_title);
//    shareParams.setUrl(share_url);
//    shareParams.setImagePath(file.getAbsolutePath());
//    //shareParams.setImageUrl("http://inews.gtimg.com/newsapp_bt/0/876781763/1000");

//    4)分享音乐
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_MUSIC
//    Title	是	String	标题	最长 200 个字符
//    Text	否	String	描述	最长 600 个字符
//    MusicUrl	否	String	音乐链接	音乐文件的远程链接 ,点击播放按钮可直接播放， 以 URL 的形式传入 , 不支持本地音乐,必须以 http 或 https 开头
//    Url	是	String	跳转链接	跳转页面 url,必须以 http 或 https 开头
//    ImagePath	否	String	缩略图，本地图片路径	与 ImageUrl 二选一
//    ImageUrl	否	String	缩略图，网络图片地址	必须以 http 或 https 开头,与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_MUSIC);
//    shareParams.setTitle(share_title);
//    shareParams.setMusicUrl(share_musicurl);
//    shareParams.setUrl(music_shareUrl);
//    shareParams.setImagePath(file.getAbsolutePath());
//    //shareParams.setImageUrl("http://inews.gtimg.com/newsapp_bt/0/876781763/1000");

//    5)分享本地视频
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_VIDEO
//    VideoPath	是	String	本地视频地址	不支持网络视频
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_VIDEO);
//    shareParams.setVideoPath(MyApplication.VideoPath);
    /******************************QQ空间结束**************************/


    /******************************新浪微博开始**************************/
//    1)分享文本
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_TEXT
//    Text 是	String	文本	不超过 1999
//    ImagePath	否	String	本地图片地址	不支持网络图片,文件不超过 10M，与 ImageData 二选一
//    ImageData	否	Bitmap 本地图片 bitmap	不支持网络图片,不超过 2M，与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_IMAGE);
//    shareParams.setText(share_text);
//    shareParams.setImagePath(file.getAbsolutePath());

//    2)分享图片
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_IMAGE
//    Text	否	String	文本	不超过 1999
//    ImagePath	是	String	本地图片地址	不支持网络图片,文件不超过 10M，与 ImageData 二选一
//    ImageData	是	Bitmap	本地图片 bitmap	不支持网络图片,不超过 2M，与 ImagePath 二选一
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_IMAGE);
//    shareParams.setText(share_text);
//    shareParams.setImagePath(file.getAbsolutePath());

//    3)分享链接
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_WEBPAGE
//    Text	否	String	文本	不超过1999
//    ImagePath	否	String	本地图片地址	不支持网络图片,文件不超过 10M，与 ImageData 二选一
//    ImageData	否	Bitmap	本地图片 bitmap	不支持网络图片,不超过 2M，与 ImagePath 二选一
//    Url	是	String	跳转链接	长度不超过 512
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_WEBPAGE);
//    shareParams.setText(share_text);
//    shareParams.setImagePath(file.getAbsolutePath());
//    shareParams.setUrl(share_url);
    /******************************新浪微博结束**************************/
    /******************************新浪微博私信开始**************************/
//    新浪微博私信

//    1)分享链接
//    参数	是否必须	参数类型	参数说明	备注
//    ShareType	是	int	分享类型	Platform.SHARE_WEBPAGE
//    Text	否	String	文本	不超过1024
//    Title	否	String	标题	不超过512
//    ImagePath	否	String	本地图片地址	不支持网络图片,文件不超过 32K，与 ImageData 二选一
//    ImageData	否	Bitmap	本地图片 bitmap	不支持网络图片,不超过 32K，与 ImagePath 二选一
//    Url	是	String	跳转链接	长度不超过512
//    ShareParams shareParams = new ShareParams();
//    shareParams.setShareType(Platform.SHARE_WEBPAGE);
//    shareParams.setText(share_text);
//    shareParams.setImagePath(file.getAbsolutePath());
//    shareParams.setUrl(share_url);

    /******************************新浪微博私信结束**************************/
}
