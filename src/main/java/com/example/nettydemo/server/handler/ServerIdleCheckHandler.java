package com.example.nettydemo.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 设置连接过期关闭
 */
@Slf4j
public class ServerIdleCheckHandler extends IdleStateHandler {

    public ServerIdleCheckHandler() {
        //10秒关闭
        super(10, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        if (evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT) {
            log.info("关闭一个空闲连接");
            ctx.close();
            return;
        }

        super.channelIdle(ctx, evt);
    }
}
