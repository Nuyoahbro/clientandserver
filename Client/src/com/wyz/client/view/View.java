package com.wyz.client.view;

import com.wyz.client.service.MessageClientService;
import com.wyz.client.service.UserClientService;
import com.wyz.client.utils.Utility;
import com.wyz.common.Message;



//客户端的菜单界面
public class View {
    public String password;

    private boolean loop=true;//控制是否显示菜单
    private String key="";//接收用户的键盘输入
    private UserClientService userClientService=new UserClientService();//对象是用于登录服务、注册用户
    private MessageClientService messageClientService=new MessageClientService();//对象用户的私聊
    public static void main(String[] args){
        new View().mainMenu();
        System.out.println("客户端退出系统");
    }
    //显示主菜单
    private void mainMenu(){
        while(loop){
            System.out.println("========欢迎登录网络通信系统==========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择:");
            key= Utility.readString(1);
            //根据用户的输入，来处理不同的逻辑
            switch(key){
                case "1":
                    System.out.print("请输入用户号：");
                    String userID=Utility.readString(50);
                    System.out.print("请输入密码：");
                    String Pwd=Utility.readString(50);
                    //到服务端验证该用户是否合法
                    if(userClientService.checkUser(userID,password)){
                        System.out.println("========欢迎(用户"+userID+"登录成功“)==========");
                        //进入到二级菜单
                        while(loop){
                            System.out.println("\n========网络通信系统二级菜单(用户"+userID+")=======");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入你的选择");
                            key=Utility.readString(1);
                            switch(key){
                                case "1":
                                    //获取用户的列表
                                    userClientService.onlineFriendList();;
                                    break;
                                case "2":
                                    System.out.println("群发消息");
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户号（在线）");
                                    String getterID=Utility.readString(50);
                                    System.out.println("请输入想说的话：");
                                    String content=Utility.readString(100);
                                    //将消息发送给服务端
                                    messageClientService.sendMessageToOne(content,userID,getterID);

                                    System.out.println("私聊消息");
                                    break;
                                case "4":
                                    System.out.println("发送文件");
                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop=false;
                                    break;
                            }
                        }
                    }else{//登录失败
                        System.out.println("==========登录失败==========");
                    }
                    break;
                case "9":
                    //调用方法，给服务器发送一个退出系统的message

                    loop=false;
                    break;
            }

        }
    }

}
