package com.my;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by paul on 2015/6/9.
 */
public class BeatHeartHanddler extends SimpleChannelInboundHandler {
    private volatile int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.pipeline() + "连接");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        count = 0;
        System.out.println("收到消息");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if (evt instanceof IdleStateEvent){
           IdleStateEvent event = (IdleStateEvent) evt;
           if (event.state().equals(IdleState.READER_IDLE)){
               System.out.println("READER_IDLE " + ++count);
           }else if (event.state().equals(IdleState.WRITER_IDLE)){
               ctx.writeAndFlush("hello");
               System.out.println("WRITER_IDLE");
           }else if (event.state().equals(IdleState.ALL_IDLE)){
               System.out.println("ALL_IDLE");
               ctx.close();
           }
       }
        super.userEventTriggered(ctx, evt);
    }
}
