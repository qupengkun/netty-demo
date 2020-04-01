package com.example.nettydemo.client.codec;

import com.example.nettydemo.common.Operation;
import com.example.nettydemo.common.RequestMessage;
import com.example.nettydemo.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 消息发送前转换，不用每次都new RequestMessage,而是直接操作Operation
 */
public class OperationToRequestMessageEncoder extends MessageToMessageEncoder<Operation> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Operation operation, List<Object> out) throws Exception {

        //TODO IdUtil.nextId()变为传入进来的
        //传入一个数据，组装为长度2的key:value数据
        RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), operation);
        //将数据传出
        out.add(requestMessage);
    }
}
