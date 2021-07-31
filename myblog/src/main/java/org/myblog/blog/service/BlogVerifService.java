package org.myblog.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Update;
import org.myblog.blog.entity.BlogVerif;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface BlogVerifService extends IService<BlogVerif> {

    BlogVerif getVerif(String uid,String cid);

    HashMap<String,Object> getVerificationCode();

    void truncate();

    boolean checkVerif(String uid, String cid, String x);

    List<HashMap<String, Object>> listAdminVerifList(String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminVerifs(Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminVerifs(Page<BlogVerif> page);

    boolean adminVerifChange(BlogVerif blogVerif, String cookieUid, LocalDateTime nowTime);

    boolean adminVerifDelete(String vids, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultAdminVerif(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);

}
