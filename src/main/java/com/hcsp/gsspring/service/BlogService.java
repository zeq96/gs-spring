package com.hcsp.gsspring.service;

import com.hcsp.gsspring.dao.BlogDao;
import com.hcsp.gsspring.entity.Blog;
import com.hcsp.gsspring.entity.BlogListResponse;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public BlogListResponse selectBlogsByPage(Integer page, Integer pageSize, Integer userId) {

        try {
            Integer count = blogDao.selectBlogCount(userId);
            List<Blog> blogs = blogDao.selectBlogs(page, pageSize, userId);
            int totalPage = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
            return BlogListResponse.success(blogs, count, page, totalPage);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogListResponse.failure("系统异常");
        }

    }
}
