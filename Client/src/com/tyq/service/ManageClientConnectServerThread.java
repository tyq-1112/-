package com.tyq.service;

import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理客户端连接到服务器端的线程
 */
public class ManageClientConnectServerThread {
    //把多个线程放入一个HashMap集合，key为用户id , value 就是线程
    private static ConcurrentHashMap<String , ClientConnectServerThread> hm = new ConcurrentHashMap<>();

    //将线程加入到集合
    public static void addToHash(String key , ClientConnectServerThread connectServerThread){
        hm.put(key , connectServerThread);
    }

    //得到线程
    public static ClientConnectServerThread getThread(String key){
        return hm.get(key);
    }

}
