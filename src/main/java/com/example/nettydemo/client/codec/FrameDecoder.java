package com.example.nettydemo.client.codec;


import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 解决服务器端响应
 * 处理粘包半包
 * 解码出没有沾包和半包问题的ByteBuf
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {
    public FrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
