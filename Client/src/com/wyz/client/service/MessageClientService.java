package com.wyz.client.service;

import com.wyz.common.Message;
import com.wyz.common.MessageTpye;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

//该类与服务端提供和消息相关的服务方法
public class MessageClientService {
    public void sendMessageToOne(String content,String senderID,String getterID){
        //内容，发送用户id，接收用户id
        //构建message
        Message message=new Message();
        message.setMesType(MessageTpye.MESSAGE_COMM_MES);//普通聊天消息
        message.setSender(senderID);
        message.setGetter(getterID);
        message.setContent(content);
        message.setSendTime(new Date().toString());//发送时间设置到message对象中
        System.out.println(senderID+"对"+getterID+"说"+content);
        //发送给服务端
        try{
            ObjectOutputStream oos=new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderID).getSocket().getOutputStream());
            oos.writeObject(message);
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
