package com.example.nettydemo.common.keepalive;


import com.example.nettydemo.common.Operation;
import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class KeepaliveOperation extends Operation {

    private long time;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public KeepaliveOperationResult execute() {
        KeepaliveOperationResult orderResponse = new KeepaliveOperationResult(time);
        return orderResponse;
    }
}
