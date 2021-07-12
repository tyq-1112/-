package com.tyq.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThread {

    private static ConcurrentHashMap<String , ServerConnectClientThread> hm = new ConcurrentHashMap<>();

    public static void  addThread(String key , ServerConnectClientThread serverConnectClientThread){
        hm.put(key,serverConnectClientThread);
    }

    public static ServerConnectClientThread getThread(String key){
        return hm.get(key);
    }

    public static ConcurrentHashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //返回在线用户列表
    public static String getOnlineUser(){
        String online = "";
        Iterator<String> iterator = hm.keySet().iterator();
        while(iterator.hasNext()){
            online+=iterator.next().toString()+" ";
        }
        return online;
    }

    //删除线程
    public static void remove(String userId){
        hm.remove(userId);
    }

}
