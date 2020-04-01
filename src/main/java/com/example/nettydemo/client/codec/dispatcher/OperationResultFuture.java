package com.example.nettydemo.client.codec.dispatcher;


import com.example.nettydemo.common.OperationResult;
import io.netty.util.concurrent.DefaultPromise;

/**
 * 解决等待同步响应的问题，使不需要一个请求处理完后面才能执行
 */
public class OperationResultFuture extends DefaultPromise<OperationResult> {
}
