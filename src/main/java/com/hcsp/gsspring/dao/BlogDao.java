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
    private SqlSession sqlSession;

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
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return sqlSession.selectOne("BlogMapper.selectBlogCount", userId);
    }
}
