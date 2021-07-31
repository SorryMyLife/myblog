package org.myblog.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.myblog.blog.entity.BlogFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.myblog.blog.vo.list.FileList;

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
public interface BlogFileMapper extends BaseMapper<BlogFile> {

    @Select("SELECT bf.id as fid,bf.name as fname,bf.text,bf.html,bf.tags,bf.uid,bf.dlink,bf.create_time,bf.size,bu.icon FROM blog_file as bf LEFT JOIN blog_user as bu on bf.uid=bu.id WHERE (bf.uid = ${uid})")
    Page<FileList> getFileByUID(Page<?> page, @Param("uid") Integer uid);

    @Select("SELECT bf.id as fid,bf.name as fname,bf.text,bf.html,bf.tags,bf.uid,bf.dlink,bf.create_time,bf.size,bu.icon FROM blog_file as bf LEFT JOIN blog_user as bu on bf.uid=bu.id")
    Page<FileList> getFileList(Page<?> page);

    @Select("select id,name as title from blog_file")
    List<Map<String, Object>> getTitleList();
}
