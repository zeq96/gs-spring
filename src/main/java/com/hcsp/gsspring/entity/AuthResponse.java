package com.hcsp.gsspring.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse extends Response<User> {

    private boolean isLogin;

    private AuthResponse(ResponseStatus status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static AuthResponse failure(String msg) {
        return new AuthResponse(ResponseStatus.FAIL, msg, null, false);
    }

    public static AuthResponse success(String msg, User user) {
        return new AuthResponse(ResponseStatus.OK, msg, user, true);
    }

    @JsonProperty("isLogin")
    public boolean isLogin() {
        return isLogin;
    }
}
