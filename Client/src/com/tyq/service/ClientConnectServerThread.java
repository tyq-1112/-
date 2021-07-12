package com.tyq.service;

import com.tyq.common.Message;
import com.tyq.common.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

//socket，保持这个线程可以跟服务器端一直通信
public class ClientConnectServerThread extends Thread {
    //该线程需要持有Socket
    private Socket socket;
    private String userId;

    public ClientConnectServerThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }


    @Override
    public void run() {
        //该线程需要一直在后台与服务器通信
        System.out.println("\n" + userId + "客户端线程： 等待服务器发送的消息....");
        while (true) {
            //一直接收服务器发来的消息
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有消息发过来，则会一直堵塞在这
                Message mes = (Message) ois.readObject();
                //得到服务器发送的Message，然后做相应的业务处理

                //RES_ONLINE_USER：服务器返回在线用户列表
                if (mes.getMesType().equals(MessageType.RES_ONLINE_USER)) {
                    String[] onlineUsers = mes.getContent().split(" ");
                    System.out.println("\n======当前在线用户列表=======");
                    for (int i = 0; i < onlineUsers.length; i++) System.out.println("用户：" + onlineUsers[i]);

                    //私聊，普通文本消息
                } else if (mes.getMesType().equals(MessageType.COMMON_CONTENT)) {
                    System.out.println("\n" + mes.getSendTime());
                    System.out.println(mes.getSender() + " 对 你 说 ： " + mes.getContent());

                    //群发
                } else if (mes.getMesType().equals(MessageType.COMMON_CONTENT_ALL)) {
                    System.out.println("\n" + mes.getSendTime());
                    System.out.println(mes.getSender() + " 对 大家说 说 ： " + mes.getContent());

                    //接收文件
                }else if(mes.getMesType().equals(MessageType.SEND_FILE)){
                    System.out.println("\n"+mes.getSendTime());
                    System.out.println(mes.getSender() + " 给 你 发送文件： " + mes.getSrc());
                    //取出字节数组，通过文件输出流写出到磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(mes.getDest());
                    fileOutputStream.write(mes.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 保存文件成功");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
