package com.ph.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author ph
 * @version 1.0
 * @date 2020/6/10 9:39
 * @description NIOClient
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        //设置非阻塞
        socketChannel.configureBlocking(false);

        //提供服务器的端口，ip
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8888);

        //判断是否连接成功
        if(!socketChannel.connect(inetSocketAddress)) {

            while(!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        //连接成功就可以发送消息
        String str = "hello world";
        //通过wrap方法可以将字节数组写入buffer，大小与字节数组大小一致
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());

        //发送数据，将buffer数据写入channel
        socketChannel.write(buffer);

        System.in.read();

    }
}
