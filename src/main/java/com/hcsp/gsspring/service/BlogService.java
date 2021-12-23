package com.hcsp.gsspring.service;

import com.hcsp.gsspring.dao.BlogDao;
import com.hcsp.gsspring.entity.Blog;
import com.hcsp.gsspring.entity.BlogListResponse;
import com.hcsp.gsspring.entity.BlogResponse;
import com.hcsp.gsspring.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

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

    public BlogResponse selectBlogById(Integer id) {
        try {
            Blog blog = blogDao.selectBlogById(id);
            return BlogResponse.success("获取成功", blog);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResponse.failure("系统异常");
        }
    }

    public BlogResponse createBlog(Blog blog) {
        try {
            return BlogResponse.success("创建成功", blogDao.createBlog(blog));
        } catch (Exception e) {
            return BlogResponse.failure(e);
        }
    }

    public BlogResponse updateBlog(Integer blogId, Blog targetBlog) {
        Blog blogInDb = blogDao.selectBlogById(blogId);
        if (!Objects.nonNull(blogInDb)) {
            return BlogResponse.failure("博客不存在");
        }

        if (!Objects.equals(blogInDb.getUser().getId(), targetBlog.getUser().getId())) {
            return BlogResponse.failure("无法修改别人的博客");
        }

        try {
            targetBlog.setId(blogId);
            return BlogResponse.success("修改成功", blogDao.updateBlog(targetBlog));
        } catch (Exception e) {
            return BlogResponse.failure(e);
        }
    }

    public BlogResponse deleteBlog(Integer blogId, User user) {
        Blog blogInDb = blogDao.selectBlogById(blogId);

        if (!Objects.nonNull(blogInDb)) {
            return BlogResponse.failure("博客不存在");
        }

        if (!Objects.equals(blogInDb.getUser().getId(), user.getId())) {
            return BlogResponse.failure("无法删除别人的博客");
        }

        try {
            blogDao.deleteBlogById(blogId);
            return BlogResponse.success("删除成功", null);
        } catch (Exception e) {
            return BlogResponse.failure(e);
        }
    }

}
