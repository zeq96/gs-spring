package com.hcsp.gsspring.entity;

import java.util.List;

public class BlogListResponse extends Response<List<Blog>> {

    private Integer total;
    private Integer page;
    private Integer totalPage;

    private BlogListResponse(ResponseStatus status, String msg, List<Blog> data,
                             Integer total, Integer page, Integer totalPage) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public static BlogListResponse success(List<Blog> data, Integer total, Integer page, Integer totalPage) {
        return new BlogListResponse(ResponseStatus.OK, "获取成功", data, total, page, totalPage);
    }

    public static BlogListResponse failure(String message) {
        return new BlogListResponse(ResponseStatus.FAIL, message, null, 0, 0, 0);
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPage() {
        return totalPage;
    }
}
