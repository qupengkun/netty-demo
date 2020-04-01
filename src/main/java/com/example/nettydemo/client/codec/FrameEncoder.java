package com.example.nettydemo.client.codec;


import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 让服务器端处理粘包半包
 */
public class FrameEncoder extends LengthFieldPrepender {
    public FrameEncoder() {
        super(2);
    }
}
