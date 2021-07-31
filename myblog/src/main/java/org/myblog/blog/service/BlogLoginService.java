package org.myblog.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogLogin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.vo.admin.AdminFile;
import org.myblog.blog.vo.admin.AdminLoginList;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-30
 */
public interface BlogLoginService extends IService<BlogLogin> {

    public BlogLogin getLoginUser(String uid);

    public boolean saveOK(String uid,String ip);

    public boolean saveError(String uid,String ip);

    public Integer getErrorCount(String uid);

    List<HashMap<String, Object>> listAdminLoginList(String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminLogins(Page<BlogLogin> page);

    List<HashMap<String, Object>> getAdminLogins(Integer valueOf, Integer valueOf1);

    boolean adminLoginChange(AdminLoginList adminLoginList, String cookieUid, LocalDateTime nowTime);

    boolean adminLoginDelete(String toString, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultAdminLogin(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);

}
