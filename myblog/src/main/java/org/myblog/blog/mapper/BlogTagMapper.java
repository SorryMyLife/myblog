package org.myblog.blog.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.myblog.blog.entity.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogTagMapper extends BaseMapper<BlogTag> {

}
