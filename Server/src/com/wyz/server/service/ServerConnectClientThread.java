package com.wyz.server.service;

import com.wyz.common.Message;
import com.wyz.common.MessageTpye;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

//该类对应的对象和某个客户端保持连接
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userID;//连接到服务端的用户id
    public ServerConnectClientThread(Socket socket,String userID){
        this.socket=socket;
        this.userID=userID;
    }
    public Socket getSocket(){
        return socket;
    }
    @Override
    public void run(){//线程处于run状态，可以发送接收消息
        while(true){
            try{
                System.out.println("服务端和客户端" + userID + " 保持通信，读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                if(message.getMesType().equals(MessageTpye.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端要在线用户列表
                    System.out.println(message.getSender()+"要在线用户列表");
                    String onlineUser=ManageClinentThreads.getOnlineUser();
                    //返回message
                    //构建一个Message对象，返回给客户端
                    Message message2=new Message();
                    message2.setMesType(MessageTpye.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());
                    //写入到数据通道，返回给客服端
                    ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if(message.getMesType().equals((MessageTpye.MESSAGE_COMM_MES))){
                    //根据Message获取getterid，然后再得到对应的线程
                    ServerConnectClientThread serverConnectClientThread=ManageClinentThreads.getServerConnectClientThread(message.getGetter());
                    //得到对应socket的对象输出流，将message对象转发给指定的客户端
                    ObjectOutputStream oos=new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);//转发
                } else if(message.getMesType().equals(MessageTpye.MESSAGE_CLIENT_EXIT)){//客户端退出
                    System.out.println(message.getSender()+"退出系统");//将这个客户端对应线程从集合删除
                    ManageClinentThreads.removeServerConnectClientThread(message.getSender());
                    socket.close();
                }
                else{
                    System.out.println("其他类型的message，暂时不处理");
                }
            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }

        }
    }
}
