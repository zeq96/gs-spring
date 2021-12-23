package com.hcsp.gsspring.controller;

import com.hcsp.gsspring.entity.Blog;
import com.hcsp.gsspring.entity.BlogListResponse;
import com.hcsp.gsspring.entity.BlogResponse;
import com.hcsp.gsspring.entity.User;
import com.hcsp.gsspring.service.AuthService;
import com.hcsp.gsspring.service.BlogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class BlogController {
    private final BlogService blogService;
    private final AuthService authService;

    @Inject
    public BlogController(BlogService blogService, AuthService authService) {
        this.blogService = blogService;
        this.authService = authService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public BlogListResponse getBlogs(@RequestParam(required = false, value = "page") Integer page,
                                     @RequestParam(required = false, value = "userId") Integer userId) {
        if (page == null || page <= 0) {
            page = 1;
        }

        return blogService.selectBlogsByPage(page, 10, userId);
    }

    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResponse getBlogById(@PathVariable("blogId") Integer blogId) {
        return blogService.selectBlogById(blogId);
    }

    @PostMapping("/blog")
    @ResponseBody
    public BlogResponse createBlog(@RequestBody Map<String, String> params) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.createBlog(setBlogFromParams(params, user)))
                    .orElse(BlogResponse.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResponse.failure(e);
        }

    }

    @PatchMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResponse updateBlog(@PathVariable("blogId") Integer blogId,
                                   @RequestBody Map<String, String> params) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlog(blogId, setBlogFromParams(params, user)))
                    .orElse(BlogResponse.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResponse.failure(e);
        }
    }

    @DeleteMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResponse deleteBlog(@PathVariable("blogId") Integer blogId) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.deleteBlog(blogId, user))
                    .orElse(BlogResponse.failure("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResponse.failure(e);
        }
    }

    private Blog setBlogFromParams(Map<String, String> params, User user) {
        Blog blog = new Blog();
        String title = params.get("title");
        String content = params.get("content");
        String description = params.get("description");

        if (StringUtils.isBlank(title) || title.length() > 100) {
            throw new IllegalArgumentException("Illegal title");
        }

        if (StringUtils.isBlank(content) || content.length() > 10000) {
            throw new IllegalArgumentException("Illegal content");
        }

        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "....";
        }

        blog.setTitle(title);
        blog.setContent(content);
        blog.setDescription(description);
        blog.setUser(user);
        return blog;
    }

}
