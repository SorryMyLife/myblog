package org.myblog.blog.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.myblog.blog.entity.BlogArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {

    @Select("select id,title from blog_article")
    List<Map<String, Object>> getTitleList();

}
