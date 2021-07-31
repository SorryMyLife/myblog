package org.myblog.blog.mapper;

import org.apache.ibatis.annotations.Update;
import org.myblog.blog.entity.BlogVerif;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author test
 * @since 2021-07-30
 */
public interface BlogVerifMapper extends BaseMapper<BlogVerif> {

    @Update("truncate blog_verif")
    void truncate();
}
