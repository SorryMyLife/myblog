package org.myblog.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogAdminUser;
import org.myblog.blog.entity.BlogRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.vo.admin.AdminRole;
import org.myblog.blog.vo.admin.AdminRoleUser;

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
public interface BlogRoleService extends IService<BlogRole> {

    public String getRoleName(Integer rid);

    List<BlogRole> getRoleList(String cookieUid);

    List<HashMap<String, Object>> listAdminRole(String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminRole(IPage<BlogRole> page);

    List<HashMap<String, Object>> getAdminRole(Integer valueOf, Integer valueOf1);

    boolean adminRoleChange(AdminRole adminRole, String cookieUid, LocalDateTime nowTime);

    boolean adminRoleDelete(String toString, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultRole(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> listAdminRoleUser(String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminRoleUser(Page<BlogAdminUser> adminUserPage);

    List<HashMap<String, Object>> getAdminRoleUser(Integer valueOf, Integer valueOf1);

    boolean adminRoleUserChange(AdminRoleUser adminRoleUser, String cookieUid, LocalDateTime nowTime);

    boolean adminRoleUserDelete(String toString, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultRoleUser(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String,Object>> getRoleAdminList(String requestCookieuid);

}
