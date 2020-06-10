package com.ph.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ph
 * @version 1.0
 * @date 2020/6/9 18:04
 * @description java BIO模型测试
 */
public class BioTest {
    public static void main(String[] args) throws IOException {
        //使用线程池机制，每次有用户连接时创建一个线程
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //创建serverSocket用于连接
        ServerSocket serverSocket = new ServerSocket(8888);

        System.out.println("服务器启动");

        //等待连接
        while(true) {
            System.out.println("线程id="+Thread.currentThread().getId()+
                    "线程名字："+Thread.currentThread().getName());

            //监听连接
            final Socket socket = serverSocket.accept();

            System.out.println("有个客户端已连接");

            //创建一个线程与客户端通信
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    //具体的通信方法
                    handler(socket);
                }
            });

        }
    }


    private static void handler(Socket socket) {

        try {
            System.out.println("线程id="+Thread.currentThread().getId()+
                    "线程名字："+Thread.currentThread().getName());

            //定义一个byte数组用于缓冲
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环接收客户端发送的消息
            while(true) {

                System.out.println("线程id="+Thread.currentThread().getId()+
                        "线程名字："+Thread.currentThread().getName());

                int read = inputStream.read(bytes);
                //判断是否读取完毕
                if(read != -1) {
                    System.out.println(new String(bytes, 0 ,read));
                }else {
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
