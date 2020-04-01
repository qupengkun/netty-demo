package com.example.nettydemo.client.codec.dispatcher;

import com.example.nettydemo.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * 把OperationResultFuture请求处理的结果对应到RequestPendingCenter上
 */
public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private RequestPendingCenter requestPendingCenter;

    public ResponseDispatcherHandler(RequestPendingCenter requestPendingCenter) {
        this.requestPendingCenter = requestPendingCenter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage responseMessage) throws Exception {
        //设置steamId和结果,分别在header和body里
        //目的，让future有一个结果
        requestPendingCenter.set(responseMessage.getMessageHeader().getStreamId(), responseMessage.getMessageBody());
    }
}
