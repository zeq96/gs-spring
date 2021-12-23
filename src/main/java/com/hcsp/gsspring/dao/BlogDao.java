package com.hcsp.gsspring.dao;

import com.hcsp.gsspring.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Blog> selectBlogs(Integer page, Integer pageSize, Integer userId) {
        int offset = (page - 1) * pageSize;
        Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", pageSize);
        return sqlSession.selectList("BlogMapper.selectBlogByPage", params);
    }

    public Integer selectBlogCount(Integer userId) {
        return sqlSession.selectOne("BlogMapper.selectBlogCount", userId);
    }

    public Blog selectBlogById(Integer blogId) {
        return sqlSession.selectOne("BlogMapper.selectBlogById", blogId);
    }

    public Blog createBlog(Blog blog) {
        sqlSession.insert("BlogMapper.createBlog", blog);
        return selectBlogById(blog.getId());
    }

    public Blog updateBlog(Blog blog) {
        sqlSession.update("BlogMapper.updateBlog", blog);
        return selectBlogById(blog.getId());
    }

    public void deleteBlogById(Integer blogId) {
        sqlSession.delete("BlogMapper.deleteBlogById", blogId);
    }

}
