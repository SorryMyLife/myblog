package org.myblog.blog.mapper;

import org.apache.ibatis.annotations.Update;
import org.myblog.blog.entity.BlogRsa;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogRsaMapper extends BaseMapper<BlogRsa> {

    @Update("truncate blog_rsa")
    void truncate();
}
