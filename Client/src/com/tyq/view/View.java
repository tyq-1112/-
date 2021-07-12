package com.tyq.view;


import com.tyq.service.ChatService;
import com.tyq.service.FileService;
import com.tyq.service.UserClientService;

import java.util.Scanner;

/**
 * 客户端的菜单界面
 */
public class View {
    private boolean loop = true; //控制是否显示菜单
    private String key = "";     //键盘输入
    private String UserId ;      //用户名
    private String passwd ;     //用户密码
    private Scanner input = new Scanner(System.in);
    private UserClientService userClientService = new UserClientService();
    private ChatService chatService = new ChatService();
    private FileService fileService = new FileService();

    public View(){
        mainMenu();
    }

    //显示主菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("============欢迎登录网络通信系统=========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            key = input.nextLine();
            switch (key) {
                case "1":
                    //System.out.println("登录系统");
                    System.out.print("请输入用户名：");
                    UserId = input.nextLine();
                    System.out.print("请输入密  码：");
                    passwd = input.nextLine();
                    //封装为对象，发送到服务器，验证用户
                    if (userClientService.checkUser(UserId, passwd)) {
                        View2(UserId);
                    }else{  // 登录失败
                        System.out.println("==========登录失败========");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }

    //登录成功的菜单
    private void View2(String userId){
        System.out.println("===========欢迎 " + userId + "登录成功 ========");
        order(userId);
        String content ;    //聊天内容
        String getterId ;
        while(loop){
            switch (key){
                case "1" :
                    //System.out.println("显示用户在线列表");
                    userClientService.getOnlineUser(userId);
                    break;
                case "2" :
                    //System.out.println("群发消息");
                    System.out.print("\n请输入想对大家说的内容：");
                    content = input.nextLine();
                    chatService.sendMessageToAll(content , userId);
                    break;
                case "3" :
                    //System.out.println("私聊消息");
                    System.out.print("\n请输入想聊天的用户号(在线)：");
                    getterId = input.nextLine();
                    System.out.print("\n请输入聊天内容：");
                    content = input.nextLine();
                    chatService.sendMessageToOne(content , userId , getterId);
                    break;
                case "4" :
                    //System.out.println("发送文件");
                    System.out.print("\n请输入想把文件发送给的用户号(在线)：");
                    getterId = input.nextLine();
                    System.out.print("请输入发送文件的路径(形式：d:\\xx.jpg)");
                    String src = input.nextLine();
                    System.out.print("请输入发送至对方保存文件的路径(形式：d:\\xx.jpg)");
                    String dest = input.nextLine();
                    fileService.sendFileOne(src,dest,userId,getterId);
                    break;
                case "9" :
                    //System.out.println("退出系统");
                    userClientService.exit(userId);
                    loop = false;
                    break;
            }
            order(userId);
        }
    }

    public  void order(String userId){
        System.out.println("\n=========网络通信菜单(用户 "+ userId + ") =========");
        System.out.println("\t\t 1 显示在线用户列表");
        System.out.println("\t\t 2 群发消息");
        System.out.println("\t\t 3 私聊消息");
        System.out.println("\t\t 4 发送文件");
        System.out.println("\t\t 9 退出系统");
        System.out.print("请输入你的选择：");
        key = input.nextLine();
    }
}
