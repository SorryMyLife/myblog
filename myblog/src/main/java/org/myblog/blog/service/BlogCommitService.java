package org.myblog.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogCommit;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.vo.admin.AdminCommit;
import org.myblog.blog.vo.user.UserMessageForArticle;
import org.myblog.blog.vo.user.UserMessageForFans;
import org.myblog.blog.vo.user.UserMessageForFile;
import org.myblog.blog.vo.user.UserMessageForLike;

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
public interface BlogCommitService extends IService<BlogCommit> {

    public Integer getCommits(String articleid);

    Integer getFileCommits(String fid);

    Integer getAllCommits(String uid);

    Page<UserMessageForArticle> getMessageOnArticle(Integer valueOf, Integer valueOf1, String uid);

    Page<UserMessageForFile> getMessageOnFile(Integer valueOf, Integer valueOf1, String uid);

    Page<UserMessageForLike> getMessageOnLike(Integer valueOf, Integer valueOf1, String uid);

    Page<UserMessageForFans> getMessageOnFans(Integer valueOf, Integer valueOf1, String uid);

    List<HashMap<String, Object>> listAdminCommit(String cookieUid, Integer valueOf, Integer valueOf1);

    List<HashMap<String, Object>> getAdminCommit(Page<BlogCommit> commitPage);

    List<HashMap<String, Object>> getAdminCommit(Integer valueOf, Integer valueOf1);

    List<Map<String, Object>> getTitleList(String decrypt, Integer c_type);

    boolean adminCommitChange(AdminCommit adminCommit, String cookieUid, LocalDateTime nowTime);

    boolean adminCommitDelete(String toString, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultAdminCommit(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);
}
