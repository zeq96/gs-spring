package com.hcsp.gsspring.controller;

import com.hcsp.gsspring.entity.BlogListResponse;
import com.hcsp.gsspring.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
public class BlogController {
    private BlogService blogService;

    @Inject
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public BlogListResponse getBlogs(@RequestParam("page") Integer page, @RequestParam(required = false,value = "userId") Integer userId) {
        if (page == null || page <= 0) {
            page = 1;
        }

        return blogService.selectBlogsByPage(page, 10, userId);
    }
}
