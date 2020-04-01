package com.example.nettydemo.server.handler;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 记录server连接数
 */
@ChannelHandler.Sharable //设置为可共享
public class MetricsHandler extends ChannelDuplexHandler {

    private AtomicLong totalConnectionNumber = new AtomicLong();

    //将totalConnectionNumber保存到Metrics中（利用<dependency><groupId>io.dropwizard.metrics</groupId><artifactId>metrics-core</artifactId><version>4.1.1</version></dependency><dependency><groupId>io.dropwizard.metrics</groupId><artifactId>metrics-jmx</artifactId><version>4.1.1</version></dependency>）
    {

        MetricRegistry metricRegistry = new MetricRegistry();

        metricRegistry.register("totalConnectionNumber", new Gauge<Long>() {
            @Override
            public Long getValue() {
                return totalConnectionNumber.longValue();
            }
        });
        //将数据统计数据以console方式展示
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metricRegistry).build();
        //展示的时间周期
        consoleReporter.start(10, TimeUnit.SECONDS);

        //将数据以jmx方式展示（实时）可jmc查看
//        JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
//        jmxReporter.start();

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //成功时加
        totalConnectionNumber.incrementAndGet();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //失败时减
        totalConnectionNumber.decrementAndGet();
        super.channelInactive(ctx);
    }
}
