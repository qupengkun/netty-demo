package com.example.nettydemo.client;

import com.example.nettydemo.client.codec.*;
import com.example.nettydemo.client.codec.dispatcher.OperationResultFuture;
import com.example.nettydemo.client.codec.dispatcher.RequestPendingCenter;
import com.example.nettydemo.client.codec.dispatcher.ResponseDispatcherHandler;
import com.example.nettydemo.common.RequestMessage;
import com.example.nettydemo.common.string.StringOperation;
import com.example.nettydemo.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.net.ssl.SSLException;
import java.util.concurrent.ExecutionException;

public class Client {

    public static void main(String[] args) throws InterruptedException, ExecutionException, SSLException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        //客户端连接服务器最大允许时间，默认为30s
        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS, 30 * 1000); //10s

        NioEventLoopGroup group = new NioEventLoopGroup();
        try {

            bootstrap.group(group);

            RequestPendingCenter requestPendingCenter = new RequestPendingCenter();
            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);

            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new FrameDecoder());
                    pipeline.addLast(new FrameEncoder());

                    pipeline.addLast(new ProtocolEncoder());
                    pipeline.addLast(new ProtocolDecoder());

                    pipeline.addLast(new ResponseDispatcherHandler(requestPendingCenter));
                    pipeline.addLast(new OperationToRequestMessageEncoder());

//                    pipeline.addLast(loggingHandler);

                }
            });

            //连接服务
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8888);
            //因为future是异步执行，所以需要先连接上后，再进行下一步操作
            channelFuture.sync();

            long streamId = IdUtil.nextId();
            /**
             * 发送数据测试，按照定义的规则组装数据
             */
//            OrderOperation orderOperation =  new OrderOperation(1001, "你好啊，hi");
            RequestMessage requestMessage = new RequestMessage(streamId, new StringOperation(1234, "你好啊，hi"));

            //将future放入center
            OperationResultFuture operationResultFuture = new OperationResultFuture();
            requestPendingCenter.add(streamId, operationResultFuture);

            //发送消息
            for (int i = 0; i < 10; i++) {
                channelFuture.channel().writeAndFlush(requestMessage);
            }

            //阻塞等待结果,结果来了之后会调用ResponseDispatcherHandler去set结果
//            OperationResult operationResult = operationResultFuture.get();
//            //将结果打印
//            System.out.println("返回:"+operationResult);

            channelFuture.channel().closeFuture().get();

        } finally {
            group.shutdownGracefully();
        }

    }

}
