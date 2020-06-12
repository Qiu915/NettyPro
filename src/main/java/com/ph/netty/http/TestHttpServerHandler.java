package com.ph.netty.http;

import com.sun.jndi.toolkit.url.Uri;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author ph
 * @version 1.0
 * @date 2020/6/12 14:24
 * @description 自定义handler
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> { //指定通信的数据类型HttpObject
    //channelRead0 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //判断msg是否是httpRequest请求
        if(httpObject instanceof HttpRequest) {
            System.out.println("msg 类型="+httpObject.getClass());
            System.out.println("客户端地址="+channelHandlerContext.channel().remoteAddress());

            //获取到HttpRequest
            HttpRequest httpRequest = (HttpRequest) httpObject;
            //获取到uri
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了favicon.ico，不做响应");
                return ;
            }

            //回复消息（http协议）
            ByteBuf byteBuf = Unpooled.copiedBuffer("来自服务端的消息：不要放弃", CharsetUtil.UTF_8);
            //构造一个http响应，即HttpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK, byteBuf);
            //设置response响应信息
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            //设置响应数据长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());

            //将构建好的response返回
            channelHandlerContext.writeAndFlush(response);
        }
    }
}
