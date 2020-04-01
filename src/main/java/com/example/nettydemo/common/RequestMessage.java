package com.example.nettydemo.common;

public class RequestMessage extends Message<Operation> {
    public RequestMessage() {
    }

    /**
     *
     * @param streamId id
     * @param operation 数据体封装
     */
    public RequestMessage(Long streamId, Operation operation) {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setStreamId(streamId);
        messageHeader.setOpCode(OperationType.fromOperation(operation).getOpCode());

        this.setMessageHeader(messageHeader);

        this.setMessageBody(operation);
    }

    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }

}
