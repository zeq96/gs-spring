package com.hcsp.gsspring.entity;

public class BlogResponse extends Response<Blog> {

    private BlogResponse(ResponseStatus status, String msg, Blog data) {
        super(status, msg, data);
    }

    public static BlogResponse success(String msg, Blog data) {
        return new BlogResponse(ResponseStatus.OK, msg, data);
    }

    public static BlogResponse failure(String msg) {
        return new BlogResponse(ResponseStatus.FAIL, msg, null);
    }

    public static BlogResponse failure(Exception e ) {
        return new BlogResponse(ResponseStatus.FAIL, e.getMessage(), null);
    }
}
