package com.cnode.wephone.cnode.entity;

/**
 * Created by ASUS on 2016/3/21.
 * 作者类
 */
public class Author {
    private String id;
    private String loginname;
    private String avatar_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
