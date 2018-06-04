package jpush.qyq.com.myapplication;

import android.app.Application;

import com.xxy.jshare.wxapi.JPushApi;
import com.xxy.jshare.wxapi.JShare;


public class WXApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JShare.init(this,
                BuildConfig.weChatAppId,
                BuildConfig.weChatSecret,
                BuildConfig.QQAppId,
                BuildConfig.QQAppKey,
                BuildConfig.sinaAppKey,
                BuildConfig.sinaAppSecret,
                BuildConfig.sinaRedirectUrl);
        JPushApi.init(this, BuildConfig.APPLICATION_ID);
    }

}
