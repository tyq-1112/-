package com.tyq.service;

import com.tyq.common.Message;
import com.tyq.common.MessageType;

import java.io.*;
import java.util.Date;

/**
 * 该对象/类完成 文件传输 服务
 */
public class FileService {

    /**
     *
     * @param src 源文件路径
     * @param dest 该文件传输到对方的路径
     * @param senderId 发送id
     * @param getterId 接收id
     */
    public static void sendFileOne(String src , String dest , String senderId , String getterId){
        //封装到Message
        Message message = new Message();
        message.setMesType(MessageType.SEND_FILE);
        message.setSendTime(new Date().toString());
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        //将文件读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);  //将src文件读入到字节数组
            message.setFileBytes(fileBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("\n" + senderId + " 给 " + getterId + " 发送文件： " + src);
        ClientConnectServerThread thread = ManageClientConnectServerThread.getThread(senderId);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
