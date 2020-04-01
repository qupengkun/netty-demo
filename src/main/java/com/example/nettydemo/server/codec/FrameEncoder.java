package com.example.nettydemo.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;


/**
 * 让客户端也需要处理粘包半包
 *
 */
public class FrameEncoder extends LengthFieldPrepender {


    /**
     *
     *  lengthFieldLength 长度是多少 为2  key:value
     */
    public FrameEncoder() {
        super(2);
    }
}
