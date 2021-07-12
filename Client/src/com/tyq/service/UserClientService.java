package com.tyq.service;

import com.tyq.common.Message;
import com.tyq.common.MessageType;
import com.tyq.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 该类完成用户登录验证等功能服务
 */
public class UserClientService {

    private User user = new User();
    private Socket socket ;

    //根据userId 和 pwd 到服务器验证用户是否合法
    public boolean checkUser(String userId , String pwd){
        boolean res = false;
        user.setUserId(userId);
        user.setPasswd(pwd);
        //可以连接服务端，发送对象
        try {
            //拿到socket
            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999); // 本地
            //得到ObjectOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //发送user对象
            oos.writeObject(user);

            //读取从服务器回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message mes = (Message)ois.readObject();

            //判断是否验证成功
            if(mes.getMesType().equals(MessageType.LOGIN_SUCCESS)) {   //登录成功
                //如果成功，需要启用一个线程类，维护这个socket，保持这个线程可以跟服务器端一直通信
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket,userId);
                clientConnectServerThread.start();
                //将线程放入集合管理
                ManageClientConnectServerThread.addToHash(userId , clientConnectServerThread);
                res = true;
            }else{    //登录失败
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    //向服务器请求在线用户列表
    public void getOnlineUser(String userId){
        //发送一个Message
        Message mes = new Message();
        mes.setMesType(MessageType.GET_ONLINE_USER);
        mes.setSender(userId);
        try {
            //从管理线程的集合中，通过id得到这个客户端线程
            ClientConnectServerThread thread = ManageClientConnectServerThread.getThread(userId);
            ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(mes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //退出客户端，并给服务端发送一个退出系统的Message对象
    public void exit(String userId){
        Message message = new Message();
        message.setSender(userId);
        message.setMesType(MessageType.CLIENT_EXIT);
        ClientConnectServerThread thread = ManageClientConnectServerThread.getThread(userId);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(userId + " 退出系统");
            System.exit(0); // 结束进程
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
