package org.myblog.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogCode;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.vo.admin.AdminCodeList;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogCodeService extends IService<BlogCode> {
    public void truncate();

    String[] getCode(int codelen, int codeheight);

    BlogCode getBlogCode(String uid,String code);

    List<HashMap<String, Object>> listAdminCodeList(String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminCodes(Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminCodes(Page<BlogCode> page);

    boolean adminCodeChange(BlogCode blogCode, String cookieUid, LocalDateTime nowTime);

    boolean adminCodeDelete(String toString, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultAdminCode(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);

}
