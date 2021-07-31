package org.myblog.blog.service;

import org.myblog.blog.entity.BlogFans;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogFansService extends IService<BlogFans> {

    Boolean save(String requestCookieUid, String uid);

    Boolean delFans(String requestCookieUid, String uid);

    Boolean isFans(String requestCookieUid, String toString);

    Integer getFansByUID(Integer uid);

}
