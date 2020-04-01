package com.example.nettydemo.common.string;

import com.example.nettydemo.common.OperationResult;
import lombok.Data;

@Data
public class StringOperationResult extends OperationResult {

    private final int id;
    private final String message;
    private final boolean complete;

}
