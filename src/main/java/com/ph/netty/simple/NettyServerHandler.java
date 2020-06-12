package com.ph.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author ph
 * @version 1.0
 * @date 2020/6/12 8:40
 * @description 自定义netty handler 需要继承netty规定好的某个HandlerAdapter（规范）
 * 这是我们自定义一个handler，才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //实际读取数据（这里我们可以读取客户端发送的数据）
    //ChannelHandlerContext :上下文对象，含有管道pipeLine，通道channel，地址
    // Object msg :就是客户端发送的数据，默认Object
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx :"+ctx);
        //将msg转成ByteBuf
        //ByteBuf是netty提供的，不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息：buf:"+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址："+ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write + Flush
        //将数据写入到缓存，并刷新
        //一般来讲，我们对这个要发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端",CharsetUtil.UTF_8));
    }
    //处理异常发送的情况
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
