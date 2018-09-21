package com.onemeter.entity;

/**
 * 描述：登陆后返回的用户信息类
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/9 11:21
 * 备注：
 */
public class userInfo {
    private String username;//用户名
    private int  uid;//用户id
    private String  token;//秘钥
    private String  appVerionCode;//app版本号（用于检测版本更新）
    private String  appApkUrl;//新版apk下载路径
    private String  desc;//版本描述

    public String getAppVerionCode() {
        return appVerionCode;
    }

    public void setAppVerionCode(String appVerionCode) {
        this.appVerionCode = appVerionCode;
    }

    public String getAppApkUrl() {
        return appApkUrl;
    }

    public void setAppApkUrl(String appApkUrl) {
        this.appApkUrl = appApkUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
