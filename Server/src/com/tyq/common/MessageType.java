package com.tyq.common;

/**
 * 表示消息类型
 */
public interface MessageType {
    //不同的值代表不同的消息类型
    String LOGIN_SUCCESS = "1";  //表示登录成功
    String LOGIN_FAIL = "2" ; //表示登录失败
    String COMMON_CONTENT = "3" ; //普普通通的文本信息
    String GET_ONLINE_USER = "4" ; //要求返回在线用户列表
    String RES_ONLINE_USER = "5" ; //返回在线用户列表
    String CLIENT_EXIT = "6" ; //客户端请求退出
    String COMMON_CONTENT_ALL = "7" ; // 群发
    String SEND_FILE = "8" ;  //发送文件
}
