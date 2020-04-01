package com.example.nettydemo.common.string;

import com.example.nettydemo.common.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class StringOperation extends Operation {

    //key
    private int id;
    //字符串类型数据体
    private String message;

    public StringOperation(int id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public StringOperationResult execute() {
//        log.info("string's executing startup with stringRequest: " + toString());
        //模拟业务处理
//        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
//        log.info("string's executing complete");
        return new StringOperationResult(id, message, true);
    }
}
