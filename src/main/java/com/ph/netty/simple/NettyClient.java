package com.ph.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author ph
 * @version 1.0
 * @date 2020/6/12 9:06
 * @description netty client
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端线程组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try{
            //创建客户端启动对象
            //注意客户端使用的ServerBootStrap而是Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            //试着启动参数
            bootstrap.group(eventExecutors)//设置线程组
                    .channel(NioSocketChannel.class)//设置客户端通道的实现类（通过反射机制）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyClienthandler());//加入自己的处理器
                        }
                    });

            //启动客户端去连接服务器
            //关于channelFuture要分析设计到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888).sync();

            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventExecutors.shutdownGracefully();
        }

    }
}
