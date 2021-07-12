package com.tyq.common;

import java.io.Serializable;

/**
 * 表示这是一个用户/客户信息
 */
//如果一个对象希望通过对象流的方式读取，需要序列化
public class User implements Serializable {
    private static final long serialVersionUID = 1l ;  //增强兼容性
    private String userId ;   //用户Id/用户名
    private String passwd ;   //用户密码

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public User (){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
