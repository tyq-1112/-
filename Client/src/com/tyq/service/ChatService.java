package com.tyq.service;

import com.tyq.common.Message;
import com.tyq.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * 该类/对象，提供和消息相关的服务方法
 */
@SuppressWarnings("all")
public class ChatService {

    /**
     * 私聊
     * @param content 发送的内容
     * @param senderId 发送者
     * @param getterId 接收者
     */
    public void sendMessageToOne(String content , String senderId , String getterId){
        //构建Message
        Message message = new Message();
        message.setMesType(MessageType.COMMON_CONTENT);
        message.setSender(senderId);
        message.setContent(content);
        message.setGetter(getterId);
        message.setSendTime(new Date().toString());
        System.out.println("\n你对 " + getterId + " 说 ： " + content);
        //拿到socket
        ClientConnectServerThread thread = ManageClientConnectServerThread.getThread(senderId);
        Socket socket = thread.getSocket();
        //发送到服务器端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 群聊
     * @param content 对大家说的话
     * @param senderId 发送者
     */
    public void sendMessageToAll(String content , String senderId){
        //构建Message
        Message message = new Message();
        message.setMesType(MessageType.COMMON_CONTENT_ALL);
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        System.out.println("\n你对 大家 说 ： " + content);
        //拿到socket
        ClientConnectServerThread thread = ManageClientConnectServerThread.getThread(senderId);
        Socket socket = thread.getSocket();
        //发送到服务器端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
