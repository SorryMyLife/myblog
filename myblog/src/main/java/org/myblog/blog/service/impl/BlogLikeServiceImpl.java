package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.myblog.blog.entity.BlogLike;
import org.myblog.blog.mapper.BlogLikeMapper;
import org.myblog.blog.service.BlogLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike> implements BlogLikeService {

    @Override
    public Integer getLikes(String id) {
        LambdaQueryWrapper<BlogLike> lambdaQuery = Wrappers.<BlogLike>lambdaQuery();
        lambdaQuery.eq(BlogLike::getLikeId,id);
        return this.count(lambdaQuery);
    }
}
