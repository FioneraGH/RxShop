package com.centling.util;

/**
 * UserInfoManager
 * Created by fionera on 17-2-28 in sweeping_robot.
 */
@SuppressWarnings("unused")
public class UserInfoManager {

    public static String getNickName() {
        return SPUtil.getString("userNickName");
    }

    public static void setNickName(String s) {
        SPUtil.setString("userNickName", s);
    }

    public static String getTel() {
        return SPUtil.getString("userTel");
    }

    public static void setTel(String s) {
        SPUtil.setString("userTel", s);
    }

    public static String getAvatar() {
        return SPUtil.getString("userAvatar");
    }

    public static void setAvatar(String s) {
        SPUtil.setString("userAvatar", s);
    }

    public static void clearAll() {
        SPUtil.clearAll();
        SPUtil.setBoolean("isSplashed", true);
    }
}
