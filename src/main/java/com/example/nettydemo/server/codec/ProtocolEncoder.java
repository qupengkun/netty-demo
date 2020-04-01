package com.example.nettydemo.server.codec;

import com.example.nettydemo.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 将responseMessage转化为ByteBuf
 *
 * */
public class ProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage responseMessage, List<Object> out) throws Exception {

        //分配buffer 不推荐ByteBufAllocator.DEFAULT.buffer()方式，会造成对外内存、对内内存或内存池、非内存池切换时问题，因为server启动时可以指定alloc如换一种方式，则自己声明的buffer会不一致
        ByteBuf buffer = ctx.alloc().buffer();

        responseMessage.encode(buffer);
        //输出buffer
        out.add(buffer);
    }
}
