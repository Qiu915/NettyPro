package com.ph.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ph
 * @version 1.0
 * @date 2020/6/12 8:22
 * @description nettyServer
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {

        //创建bossGroup和workerGroup
        //说明：
        //1.创建两个线程组bossGroup和workerGroup
        //2.bossGroup只负责处理连接请求，真正和客户端的业务处理交给workerGroup线程组完成
        //3.bossGroup和workerGroup含有的子线程（NioEventLoop）的个数默认是cpu核数*2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数
        ServerBootstrap bootstrap = new ServerBootstrap();

        try{
            //使用链式编程来配置参数
            bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //给pipeLine设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });//给我们的workGroup的eventLoop对应的管道设置处理器

            System.out.println("服务器准备好");
            //绑定端口并且设置同步，生成一个ChannelFuture对象
            ChannelFuture channelFuture = bootstrap.bind(8888).sync();

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }



    }
}
