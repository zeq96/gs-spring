package com.hcsp.gsspring.entity;

/**
 * 接口响应类型基类
 * @param <T>
 */
public abstract class Response<T> {

    private ResponseStatus status;
    private String msg;
    private T data;

    protected Response(ResponseStatus status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public enum ResponseStatus {
        OK("ok"),
        FAIL("fail");

        private String status;

        ResponseStatus(String status) {
            this.status = status;
        }
    }
}
