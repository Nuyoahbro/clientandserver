package com.wyz.client.view;
//客户端的菜单界面
public class View {
    private boolean loop=true;//控制是否显示菜单
    private String key="";//接收用户的键盘输入
    //显示主菜单
    private void mainMenu(){
        while(loop){
            System.out.println("========欢迎登录网络通信系统==========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 1 退出系统");
        }
    }

}
