package org.myblog.blog.service;

import org.myblog.blog.entity.BlogLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogLikeService extends IService<BlogLike> {

    public Integer getLikes(String id);

}
