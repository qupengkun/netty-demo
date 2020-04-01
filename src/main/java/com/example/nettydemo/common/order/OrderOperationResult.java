package com.example.nettydemo.common.order;

import com.example.nettydemo.common.OperationResult;
import lombok.Data;

@Data
public class OrderOperationResult extends OperationResult {

    private final int tableId;
    private final String dish;
    private final boolean complete;

}
