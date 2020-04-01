package com.example.nettydemo.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


/**
 * 第一层decoder
 * 处理粘包半包
 * 解码出没有沾包和半包问题的ByteBuf
 *
 * 流程：因为是server端，所以先接收请求再编解码，客户端与之相反,为先发送请求编解码
 * 请求经过第一级别decoder FrameDecoder(解决TCP协议的粘包半包)
 * -》解出byteBuf之后到第二层decoder ProtocolDecoder（给业务层做处理，将byteBuf转化为requestMessage）
 * -》将信息传输给InboundHandler ServerProcessHandler（实际业务处理，处理后将数据发出去）
 *
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {


    /**
     *
     *  maxFrameLength 最大长度
     *  lengthFieldOffset 传输字段的位移，设置0开始
     *  lengthFieldLength 长度是多少 为2  key:value
     *  lengthAdjustment 是否调整length 否
     *  initialBytesToStrip 是否将解码出的信息头字段去掉， 2为去掉
     */
    public FrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
