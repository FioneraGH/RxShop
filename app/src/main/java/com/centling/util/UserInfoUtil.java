package com.centling.util;

import android.text.TextUtils;

public class UserInfoUtil {

    public static Boolean isKFOnline() {
        return SPUtil.getBoolean("is_online");
    }

    public static String getQQ() {
        return SPUtil.getString("member_qq");
    }

    public static String getLevel() {
        return SPUtil.getString("member_level");
    }

    public static String getLevel_digit() {
        return SPUtil.getString("member_level_digit");
    }

    public static String getRealName() {
        return SPUtil.getString("realName");
    }

    public static String getName() {
        return SPUtil.getString("userName");
    }

    public static String getNickName() {
        return SPUtil.getString("userNickName");
    }

    public static String getKey() {
        return SPUtil.getString("userKey");
    }

    public static String getMobile() {
        return SPUtil.getString("userMobile");
    }

    public static String getId() {
        return SPUtil.getString("userId");
    }

    public static String getSex() {
        return SPUtil.getString("userSex");
    }

    public static String getHeight() {
        return SPUtil.getString("userHeight");
    }

    public static String getWeight() {
        return SPUtil.getString("userWeight");
    }

    public static String getBirth() {
        return SPUtil.getString("userBirth");
    }

    public static String getVip() {
        return SPUtil.getString("isVip");
    }

    public static String getInviteCode() {
        return SPUtil.getString("inviteCode");
    }

    public static String getAvatar() {
        return SPUtil.getString("userAvatar");
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(getKey());
    }

    public static void clearAll() {
        SPUtil.clearAll();
        SPUtil.setBoolean("isSplashed", true);
    }

}