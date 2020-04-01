package com.example.nettydemo.client.codec;

import com.example.nettydemo.common.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;


/**
 * 客户端发出请求
 * 将数据编码为二进制字节流
 *
 * 流程：因为是server端，所以先接收请求再编解码，客户端与之相反,为先发送请求编解码
 *  请求经过第一级别Encoder ProtocolEncoder(将数据编码成字节流)
 *  -》 FrameEncoder（让服务器端处理粘包半包） -》发给服务器端 -》 服务器端响应 -》客户端处理服务器返回，FrameDecoder处理粘包半包 -》ProtocolDecoder（将byteBuf编译成能处理的对象）
 *
 *
 */
public class ProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage requestMessage, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        requestMessage.encode(buffer);

        out.add(buffer);
    }
}
