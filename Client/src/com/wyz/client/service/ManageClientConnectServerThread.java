package com.wyz.client.service;

import java.util.HashMap;

//该类管理客户端连接服务器的线程的类
public class ManageClientConnectServerThread {
    //把多个线程放入到HashMap中，key就是用户id，value就是线程
    private static HashMap<String,ClientConnectServerThread> hm=new HashMap<>();

    //将某个线程加入到集合中
    public static void addClientConnectServerThread(String userID, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userID,clientConnectServerThread);
    }
    //通过userid得到对应线程
    public static ClientConnectServerThread getClientConnectServerThread(String userID){
        return hm.get(userID);
    }

}
