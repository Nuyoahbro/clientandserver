package com.wyz.server.service;

import com.wyz.common.Message;
import com.wyz.common.MessageTpye;
import com.wyz.common.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


//服务端，监听9999，等待客户端的连接并保持通信
public class Server {

    private ServerSocket ss=null;
    //创建一个集合，存放多个用户
    private static ConcurrentHashMap<String,User> vaildUsers=new ConcurrentHashMap<>();//可处理线程安全
    static{//在静态代码块，初始化validUsers
        vaildUsers.put("100",new User("100","123456"));
        vaildUsers.put("200",new User("200","123456"));
        vaildUsers.put("300",new User("300","123456"));
    }
    private boolean checkUser(String userID,String passwd){
        User user=vaildUsers.get(userID);
        if(user==null){//说明没有人在里面
            return false;
        }
        if(!user.getPassword().equals(passwd)){//id正确 密码错误
            return false;
        }
        return true;
    }
    public static void main(String[] args){

        new Server();
    }
    public Server(){//构造器
        //端口可以写在一个配置文件
        try{
            System.out.println("服务端在9999端口监听...");
            ss=new ServerSocket(9999);
            while(true){//当和某个客户端建立连接后，会一直监听
                Socket socket=ss.accept();//没有客户端连接，就会阻止
                //得到socket关联的对象输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //得到socket关联的对象的输出流
                ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();//读取客户端发送的User对象
                //创建一个Message对象，准备回复客户端
                Message message=new Message();
                //验证
                if(checkUser(u.getUserID(), u.getPassword())){//合法用户
                    message.setMesType(MessageTpye.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //创建一个线程，和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread serverConnectClientThread=new ServerConnectClientThread(socket,u.getUserID());
                    //启动该线程
                    serverConnectClientThread.start();
                    //该线程对象放入一个集合中进行管理
                    ManageClinentThreads.addClientThread(u.getUserID(),serverConnectClientThread);
                }else{//用户不合法
                    System.out.println("用户 id"+u.getUserID() +"pwd="+u.getPassword()+"验证失败");
                    message.setMesType(MessageTpye.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    //关闭socket
                    socket.close();//关闭连接
                    //退出线程
                    break;

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //如果服务端退出while，说明服务器端不再监听，因此关闭ServerSocket
            try{
                ss.close();
            }catch(IOException e){
                e.printStackTrace();

            }
        }
    }
}
