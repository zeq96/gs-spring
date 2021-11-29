package com.hcsp.gsspring.entity;

public class AuthResponse {

    String status;
    String msg;
    boolean isLogin;
    Object data;

    private AuthResponse(String status, String msg, boolean isLogin, Object data) {
        this.status = status;
        this.msg = msg;
        this.isLogin = isLogin;
        this.data = data;
    }

    public static AuthResponse failure(String msg) {
        return new AuthResponse("fail", msg, false, null);
    }

    public static AuthResponse success(String msg, Object obj) {
        return new AuthResponse("ok", msg, true, obj);
    }

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public Object getData() {
        return data;
    }
}
