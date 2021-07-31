package org.myblog.blog.mapper;

import org.apache.ibatis.annotations.Select;
import org.myblog.blog.entity.BlogRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogRoleMapper extends BaseMapper<BlogRole> {

    @Select("select br.id,br.name from blog_role as br")
    List<HashMap<String, Object>> getRoleList();
}
