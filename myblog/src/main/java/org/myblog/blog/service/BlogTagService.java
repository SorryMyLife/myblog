package org.myblog.blog.service;

import org.myblog.blog.entity.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogTagService extends IService<BlogTag> {

    public List<String> getTags(String tags);

    public Boolean isExist(String tagname);

    public void saveTags(String[] tags,Integer uid);

    List<Map<String,Object>> getTagNames(Integer uid);

    Map<String, Object> getIntoTagPage(String toString, Integer valueOf, Integer valueOf1, Integer valueOf2);

    Integer getTagsByUID(Integer uid);

}
