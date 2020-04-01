package com.example.nettydemo.server;

import com.example.nettydemo.server.codec.FrameDecoder;
import com.example.nettydemo.server.codec.FrameEncoder;
import com.example.nettydemo.server.codec.ProtocolDecoder;
import com.example.nettydemo.server.codec.ProtocolEncoder;
import com.example.nettydemo.server.handler.MetricsHandler;
import com.example.nettydemo.server.handler.ServerIdleCheckHandler;
import com.example.nettydemo.server.handler.ServerProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutionException;

/**
 * netty server 入口
 */
@Slf4j
public class Server {


    public static void main(String... args) throws InterruptedException, ExecutionException, CertificateException, SSLException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //设置channel模式，因为是server所以使用NioServerSocketChannel
        serverBootstrap.channel(NioServerSocketChannel.class);

        //最大的等待连接数量
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);
        //设置是否启用 Nagle 算法:用将小的碎片数据连接成更大的报文 来提高发送效率。
        //如果需要发送一些较小的报文，则需要禁用该算法
        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);

        //设置netty自带的log，并设置级别
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

        //thread
        //用户指定线程名
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        NioEventLoopGroup workGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        UnorderedThreadPoolEventExecutor businessGroup = new UnorderedThreadPoolEventExecutor(10, new DefaultThreadFactory("business"));

        //只能使用一个线程，因GlobalTrafficShapingHandler比较轻量级
        NioEventLoopGroup eventLoopGroupForTrafficShaping = new NioEventLoopGroup(0, new DefaultThreadFactory("TS"));

        try {
            //设置react方式
            serverBootstrap.group(bossGroup, workGroup);

            //metrics
            MetricsHandler metricsHandler = new MetricsHandler();

            //trafficShaping流量整形
            //long writeLimit 写入时控制, long readLimit 读取时控制 具体设置看业务修改
            GlobalTrafficShapingHandler globalTrafficShapingHandler = new GlobalTrafficShapingHandler(eventLoopGroupForTrafficShaping, 10 * 1024 * 1024, 10 * 1024 * 1024);


            //log
            LoggingHandler debugLogHandler = new LoggingHandler(LogLevel.DEBUG);
            LoggingHandler infoLogHandler = new LoggingHandler(LogLevel.INFO);

            //设置childHandler，按执行顺序放
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast("debugLog", debugLogHandler);
                    pipeline.addLast("tsHandler", globalTrafficShapingHandler);
                    pipeline.addLast("metricHandler", metricsHandler);
                    pipeline.addLast("idleHandler", new ServerIdleCheckHandler());

                    pipeline.addLast("frameDecoder", new FrameDecoder());
                    pipeline.addLast("frameEncoder", new FrameEncoder());
                    pipeline.addLast("protocolDecoder", new ProtocolDecoder());
                    pipeline.addLast("protocolEncoder", new ProtocolEncoder());

                    pipeline.addLast("infoLog", infoLogHandler);
                    //对flush增强，减少flush次数牺牲延迟增强吞吐量
                    pipeline.addLast("flushEnhance", new FlushConsolidationHandler(10, true));
                    //为业务处理指定单独的线程池
                    pipeline.addLast(businessGroup, new ServerProcessHandler());//businessGroup,
                }
            });

            //绑定端口并阻塞启动
            ChannelFuture channelFuture = serverBootstrap.bind(8888).sync();

            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            businessGroup.shutdownGracefully();
            eventLoopGroupForTrafficShaping.shutdownGracefully();
        }

    }

}
