package com.example.nettydemo.server.codec;

import com.example.nettydemo.common.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 第二层decoder
 * 将ByteBuf转化为业务能用的信息
 */
public class ProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 将ByteBuf转化为requestMessage
     * @param ctx
     * @param byteBuf
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.decode(byteBuf);

        //将requestMessage添加到output中
        out.add(requestMessage);
    }
}
