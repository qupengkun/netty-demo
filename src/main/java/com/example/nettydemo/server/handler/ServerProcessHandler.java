package com.example.nettydemo.server.handler;

import com.example.nettydemo.common.Operation;
import com.example.nettydemo.common.OperationResult;
import com.example.nettydemo.common.RequestMessage;
import com.example.nettydemo.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务处理handler
 * 处理第二层decoder的数据，并将数据发出去
 * 继承SimpleChannelInboundHandler好处是可以自动释放byteBuf
 */
@Slf4j
public class ServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {

        //发数据前的转换，最终转换为二进制
        //TODO 可自定义返回数据，暂时传什么返回什么
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        //
//        log.info("收到的信息：{}", JsonUtil.toJson(requestMessage));

        if (ctx.channel().isActive() && ctx.channel().isWritable()) {
            //将信息写出去 -> ProtocolEncoder
//            log.warn("返responseMessage: {}", responseMessage);
//            ctx.writeAndFlush(responseMessage);
//            ctx.writeAndFlush("接收成功"+System.currentTimeMillis());
            //TODO 返回发送者信息
        } else {
            log.error("not writable now, message dropped");
            throw new RuntimeException("返回失败");
        }
    }


}
