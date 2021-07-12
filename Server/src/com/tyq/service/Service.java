package com.tyq.service;

import com.tyq.common.Message;
import com.tyq.common.MessageType;
import com.tyq.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是服务器，监听9999端口，并一直保持通信
 */
public class Service {

    private ServerSocket serverSocket = null;
    private Message mes = new Message();
    //合法用户
    //ConcurrentHashMap ,可以处理并发的集合，
    //HashMap 在多线程下不安全
    private static ConcurrentHashMap<String , User> validUsers = new ConcurrentHashMap<>();

    static {
        validUsers.put("123",new User("123","123456"));
        validUsers.put("234",new User("234","234567"));
        validUsers.put("345",new User("345","345678"));
        validUsers.put("456",new User("456","456789"));
        validUsers.put("567",new User("567","567890"));
    }

    public Service(){
        System.out.println("***服务器在9999端口监听");
        try {
            //端口可以写在配置文件
            serverSocket = new ServerSocket(9999);
            while(true){   //当和某个客户端连接后，会继续监听
                Socket socket = serverSocket.accept();
                //得到socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //得到socket关联的对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                User user = (User)ois.readObject();  //读取客户端发送的User对象

                //验证
                if(checkUser(user)){
                    //登录成功，需要返回一个Message对象给连接成功的客户端
                    mes.setMesType(MessageType.LOGIN_SUCCESS);
                    oos.writeObject(mes);

                    //创建一个线程，和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket,user.getUserId());
                    serverConnectClientThread.start();
                    //该线程对象，放入到一个集合中，进行管理
                    ManageClientThread.addThread(user.getUserId(),serverConnectClientThread);
                }else{
                    mes.setMesType(MessageType.LOGIN_FAIL);
                    System.out.println("用户 " + user.getUserId() + " 登录失败");
                    oos.writeObject(mes);
                    socket.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                //如果服务器退出了while，说明服务器不在监听，因此关闭ServerSocket
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //验证用户是否合法
    private boolean checkUser(User u){
        String userId = u.getUserId() , pwd = u.getPasswd() ;
        User user = validUsers.get(userId);
        if(user == null) return false;
        if(user.getPasswd().equals(pwd) && user.getUserId().equals(userId)) return true;
        return false;
    }

}
