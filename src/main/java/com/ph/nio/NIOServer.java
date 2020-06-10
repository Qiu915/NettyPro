package com.ph.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ph
 * @version 1.0
 * @date 2020/6/9 18:07
 * @description NIOServer
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {

        //创建ServerSocketChannel对象
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //创建selector对象
        Selector selector = Selector.open();

        //绑定一个端口8888，在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));

        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到selector，关心的事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while(true) {
            //判断当前是否有事件发生
            if(selector.select(1000) == 0) {
                System.out.println("等待一秒，无连接");
                continue;
            }

            //处理事件,获取selectionKeys集合，通过selectionKeys获取channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历selectionKeys集合，进行事件处理

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while(keyIterator.hasNext()) {
                //获取selectionKey
                SelectionKey key = keyIterator.next();

                //根据key的条件，进行相应的事件处理
                if(key.isAcceptable()) {
                    //获取channel,给客户端生成channel
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);

                    //注册到selector,关注事件为OP_ACCEPT，同时关联一个byteBuffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("建立连接");
                }

                if(key.isReadable()) {
                    //通过key，反向获取到selectionKey
                    SocketChannel channel =(SocketChannel) key.channel();

                    //获取到channel关联的buffer
                    ByteBuffer buffer =(ByteBuffer) key.attachment();

                    //将数据读入buffer
                    channel.read(buffer);

                    System.out.println("来自客户端的消息" + new String(buffer.array()));
                }

                //从selectionKeys集合中移除key，防止多线程重复操作
                keyIterator.remove();

            }
        }


    }
}
