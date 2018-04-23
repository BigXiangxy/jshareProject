package com.xxy.jshare.wxapi;

import android.content.Context;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by QYG_XXY on 0013 2018/4/13.
 */

public final class JPushApi {

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        JPushInterface.init(context);
    }

    /**
     * 设置DEBUG
     * <p>
     * 在init之前调用
     *
     * @param debugEnalbed
     */
    public static void setDebugMode(boolean debugEnalbed) {
        JPushInterface.setDebugMode(debugEnalbed);
    }

    /**
     * 停止推送服务。
     * 调用了本 API 后，JPush 推送服务完全被停止。具体表现为：
     * 收不到推送消息
     * 极光推送所有的其他 API 调用都无效,不能通过 JPushInterface.init 恢复，需要调用resumePush恢复。
     *
     * @param context
     */
    public static void stopPush(Context context) {
        JPushInterface.stopPush(context);
    }

    /**
     * 恢复推送服务。
     * 调用了此 API 后，极光推送完全恢复正常工作。
     *
     * @param context
     */
    public static void resumePush(Context context) {
        JPushInterface.resumePush(context);
    }

    /**
     * 用来检查 Push Service 是否已经被停止
     * SDK 1.5.2 以上版本支持。
     *
     * @param context
     * @return
     */
    public static boolean isPushStopped(Context context) {
        return JPushInterface.isPushStopped(context);
    }

    /**
     * 调用此 API 来设置别名。
     * 需要理解的是，这个接口是覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param alias    每次调用设置有效的别名，覆盖之前的设置。
     *                 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     *                 限制：alias 命名长度限制为 40 字节。（判断长度需采用UTF-8编码）
     */
    public static void setAlias(Context context, int sequence, String alias) {
        JPushInterface.setAlias(context, sequence, alias);
    }


    /**
     * 调用此 API 来删除别名。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    public static void deleteAlias(Context context, int sequence) {
        JPushInterface.deleteAlias(context, sequence);
    }

    /**
     * 调用此 API 来查询别名。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    public static void getAlias(Context context, int sequence) {
        JPushInterface.getAlias(context, sequence);
    }


    /**
     * 调用此 API 来设置标签。
     * 需要理解的是，这个接口是覆盖逻辑，而不是增量逻辑。即新的调用会覆盖之前的设置。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性
     * @param tags     限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
     *                 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
     */
    public static void setTags(Context context, int sequence, Set<String> tags) {
        JPushInterface.setTags(context, sequence, tags);
    }


    /**
     * 调用此 API 来新增标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param tags     每次调用至少新增一个 tag。
     *                 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     *                 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
     *                 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
     */
    public static void addTags(Context context, int sequence, Set<String> tags) {
        JPushInterface.addTags(context, sequence, tags);
    }


    /**
     * 调用此 API 来删除指定标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param tags     每次调用至少删除一个 tag。
     *                 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     *                 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
     *                 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
     */
    public static void deleteTags(Context context, int sequence, Set<String> tags) {
        JPushInterface.deleteTags(context, sequence, tags);
    }

    /**
     * 调用此 API 来清除所有标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    public static void cleanTags(Context context, int sequence) {
        JPushInterface.cleanTags(context, sequence);
    }


    /**
     * 调用此 API 来查询所有标签。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     */
    public static void getAllTags(Context context, int sequence) {
        JPushInterface.getAllTags(context, sequence);
    }


    /**
     * 调用此 API 来查询指定tag与当前用户绑定的状态。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.0.7
     *
     * @param context
     * @param sequence 用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param tag      tag
     */

    public static void checkTagBindState(Context context, int sequence, String tag) {
        JPushInterface.checkTagBindState(context, sequence, tag);
    }

    /**
     * 调用此API设置手机号码。该接口会控制调用频率，频率为 10s之内最多三次。 用于短信补充功能。
     * 支持的版本
     * <p>
     * 开始支持的版本：3.1.1
     *
     * @param context
     * @param sequence     用户自定义的操作序列号, 同操作结果一起返回，用来标识一次操作的唯一性。
     * @param mobileNumber 电话号码 手机号码。如果传null或空串则为解除号码绑定操作。
     *                     限制：只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。
     */
    public static void setMobileNumber(Context context, int sequence, String mobileNumber) {
        JPushInterface.setMobileNumber(context, sequence, mobileNumber);
    }


}
