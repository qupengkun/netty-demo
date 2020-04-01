package com.example.nettydemo.common.keepalive;

import com.example.nettydemo.common.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
