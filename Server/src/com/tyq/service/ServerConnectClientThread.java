package com.tyq.service;

import com.tyq.common.Message;
import com.tyq.common.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类对应的对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread {

    private Socket socket;
    private String UserId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.UserId = userId;
    }

    @Override
    public void run() {  //这里线程处于run状态，可以发送/接收消息
        System.out.println("***用户 " + UserId + " 登录成功，正在与服务器保持通信....");
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message mes = (Message) ois.readObject();
                //得到客户端发送的Message，然后做相应的业务处理

                //客户端要求返回在线用户列表
                if (mes.getMesType().equals(MessageType.GET_ONLINE_USER)) {
                    System.out.println("****用户：" + mes.getSender() + " 要求获取在线用户列表");
                    String onlineUser = ManageClientThread.getOnlineUser();
                    //返回给客户端
                    Message message = new Message();
                    message.setMesType(MessageType.RES_ONLINE_USER);
                    message.setContent(onlineUser);
                    message.setGetter(mes.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);

                    //客户端退出系统
                } else if (mes.getMesType().equals(MessageType.CLIENT_EXIT)) {
                    System.out.println("****用户：" + mes.getSender() + " 退出系统");
                    //移除对应的线程
                    ManageClientThread.remove(mes.getSender());
                    socket.close();
                    break;

                    //私聊
                } else if (mes.getMesType().equals(MessageType.COMMON_CONTENT)) {
                    System.out.println(mes.getSendTime());
                    System.out.println(mes.getSender() + " 对 " + mes.getGetter() + " 说 ： " + mes.getContent());
                    //根据Message获取getterId，然后发送到对应的线程，
                    ServerConnectClientThread thread = ManageClientThread.getThread(mes.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(mes);  //转发，如果用户不在线，可以保持到DB

                    //群发
                }else if(mes.getMesType().equals(MessageType.COMMON_CONTENT_ALL)){
                    //遍历集合，得到所有的线程的socket，然后把Message转发
                    ConcurrentHashMap<String, ServerConnectClientThread> hm = ManageClientThread.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    System.out.println(mes.getSendTime());
                    System.out.println(mes.getSender() + " 对 大家 说 ： " + mes.getContent());
                    while(iterator.hasNext()){
                        String onlineId = iterator.next().toString();
                        if(!onlineId.equals(mes.getSender())){
                            ServerConnectClientThread thread = ManageClientThread.getThread(onlineId);
                            ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                            oos.writeObject(mes);
                        }
                    }
                    //发文件
                }else if(mes.getMesType().equals(MessageType.SEND_FILE)){
                    //根据getterId获取对应的线程，将Message转发
                    ServerConnectClientThread thread = ManageClientThread.getThread(mes.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(mes);
                    System.out.println("\n"+mes.getSendTime());
                    System.out.println(mes.getSender() + " 给 " + mes.getGetter() + " 发送文件： " + mes.getSrc());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
