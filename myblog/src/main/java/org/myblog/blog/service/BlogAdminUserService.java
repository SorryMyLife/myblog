package org.myblog.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogAdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.entity.BlogUser;
import org.myblog.blog.vo.admin.AdminPwd;
import org.myblog.blog.vo.admin.AdminUser;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-24
 */
public interface BlogAdminUserService extends IService<BlogAdminUser> {

    BlogAdminUser getAdminUser(Integer uid);

    List<HashMap<String, Object>> getAdminUsers(Page<BlogUser> userPage);

    List<HashMap<String, Object>> getAdminUsers(Integer currentpage, Integer currentsize);

    List<HashMap<String, Object>> listAdminUsers(Integer currentpage, Integer currentsize, Integer uid);

    Boolean changeAdminUser(Integer valueOf, AdminUser adminUser);

    Boolean changeAdminPwd(Integer valueOf, AdminPwd adminPwd);

    Boolean delAdminUsers(Integer uid, String uids);

    void removeAdminByUID(String uid);

    List<HashMap<String, Object>> searchMultAdminUsers(String s_types, String s_str, Integer currentpage, Integer currentsize, Integer valueOf);

    List<Map<String, Object>> getUserList(String requestCookieUid);

}
