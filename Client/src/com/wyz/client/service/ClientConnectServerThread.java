package com.wyz.client.service;

import com.wyz.common.Message;
import com.wyz.common.MessageTpye;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread{
    //该线程需要持有Socket
    private Socket socket;
    //构造器可以接受一个Socket对象
    public ClientConnectServerThread(Socket socket){
        this.socket=socket;
    }
    public  void run(){
        //因为Thread需要在后台与服务器通信，所以用while循环
        while(true){
            try{
                System.out.println("客户端线程，等待读取从服务器端发送的消息");
                ObjectInputStream ois=new ObjectInputStream(socket.getInputStream());
                Message message=(Message)ois.readObject();//如果服务器没有发送Message对象，线程会阻塞
                //判断这个message类型 然后做出相应的业务处理
                //如果读取到的是 服务端返回的在线用户列表
                if(message.getMesType().equals(MessageTpye.MESSAGE_RET_ONLINE_FRIEND)){
                    //取出在线列表信息，并显示
                    String[] onlineUser=message.getContent().split(" ");
                    System.out.println("\n=======当前在线用户列表=======");
                    for(int i=0;i< onlineUser.length;i++){
                        System.out.println("用户："+onlineUser[i]);
                    }

                }else if(message.getMesType().equals(MessageTpye.MESSAGE_COMM_MES)){
                    //把服务器端的消息显示到控制台
                    System.out.println("\n"+message.getSender()+"对"+message.getGetter()+"说："+message.getContent());
                }
                else{
                    System.out.println("是其他类型的message，暂时不处理");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    //为了更方便的得到Socket
    public Socket getSocket(){
        return socket;
    }
}