package org.myblog.blog.service;

import org.myblog.blog.entity.BlogUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.vo.LoginUser;
import org.myblog.blog.vo.user.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogUserService extends IService<BlogUser> {

    Integer getUid();

    BlogUser isLogin(LoginUser loginUser);

    UserInfo getUserInfo(BlogUser blogUser);

    UserInfo getUserInfo(String uid);

    BlogUser getUser(String uid);

    Boolean regUser(LoginUser loginUser);

    HashMap<String,Object> getUserPageInfo(String uid, Integer currentpage, Integer currentsize);

    Map<String, Object> getArticleUser(Integer uid);

}
