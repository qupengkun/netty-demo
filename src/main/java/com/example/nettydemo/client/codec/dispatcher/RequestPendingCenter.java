package com.example.nettydemo.client.codec.dispatcher;



import com.example.nettydemo.common.OperationResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 解决等待同步响应的问题，使不需要一个请求处理完后面才能执行
 */
public class RequestPendingCenter {

    private Map<Long, OperationResultFuture> map = new ConcurrentHashMap<>();

    /**
     * 发送数据时,对应于请求
     * @param streamId key
     * @param future value
     */
    public void add(Long streamId, OperationResultFuture future) {
        this.map.put(streamId, future);
    }

    /**
     * 对应于响应，根据streamId设置future的结果
     * @param streamId
     * @param operationResult
     */
    public void set(Long streamId, OperationResult operationResult) {
        //根据steamId查找future
        OperationResultFuture operationResultFuture = this.map.get(streamId);

        if (operationResultFuture != null) {
            //标记future状态为成功并移除该数据（防止map无限膨胀）
            operationResultFuture.setSuccess(operationResult);
            this.map.remove(streamId);
        }
    }


}
