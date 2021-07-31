package org.myblog.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.myblog.blog.entity.BlogArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.vo.admin.AdminArticle;
import org.myblog.blog.vo.list.HotArticleList;
import org.myblog.blog.vo.submit.SubmitArticle;

import java.time.LocalDateTime;
import java.util.HashMap;
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
public interface BlogArticleService extends IService<BlogArticle> {

    public Boolean SubmitArticleSave(SubmitArticle submitArticle, String uid, LocalDateTime now);

    List<Map<String, Object>> pageArticles(IPage<BlogArticle> page);

    List<Map<String, Object>> pageArticles(Integer currentPage , Integer currentSize);

    List<Map<String, Object>> pageArticlesByUID(IPage<BlogArticle> page);

    List<Map<String, Object>> pageArticlesByUID(Integer uid, Integer currentPage , Integer currentSize);

    List<Map<String, Object>> pageArticlesByTagID(Integer uid,Integer tagid, Integer currentPage , Integer currentSize);

    Map<String,Object> getReadArticles(String aid);

    List<Map<String, Object>> SearchPageArticles(String search_str, Integer valueOf, Integer valueOf1);

    List<Map<String, Object>> SearchOnUserPageArticles(String uid, String search_str, Integer valueOf, Integer valueOf1);

    List<HotArticleList> SearchOnUserArticles(String toString, String toString1, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminArticles(Integer currpage,Integer currsize);

    List<HashMap<String, Object>> listAdminArticle(String cookieUid, Integer valueOf, Integer valueOf1);

    boolean adminArticleChange(AdminArticle article, String uid, LocalDateTime nowTime);

    boolean adminArticleDelete(String aids, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultAdminArticle(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);

    List<Map<String, Object>> getTitleList();
}
