package com.jeeplus.modules.sys.entity;

/**
 * @author yangyang
 * @description 用户登录mc地址
 * @create 2019-07-11 09:06
 **/
public class LoginMc{
    private String userid;		// 用户id
    private String mc;		// mc地址

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }
}