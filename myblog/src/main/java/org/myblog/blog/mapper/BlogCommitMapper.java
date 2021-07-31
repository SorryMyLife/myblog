package org.myblog.blog.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.myblog.blog.entity.BlogCommit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.myblog.blog.vo.user.UserMessageForArticle;
import org.myblog.blog.vo.user.UserMessageForFans;
import org.myblog.blog.vo.user.UserMessageForFile;
import org.myblog.blog.vo.user.UserMessageForLike;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogCommitMapper extends BaseMapper<BlogCommit> {

    @Select("select bc.uid,bu2.name,bu2.icon,ba.title,ba.id as aid,bc.create_time,bc.msg as msg from blog_commit as bc LEFT JOIN blog_article as ba on ba.id =bc.id and bc.type=0 LEFT JOIN blog_user as bu on bu.id=ba.uid left join blog_user as bu2 on bu2.id=bc.uid where bu.id=${uid}")
    Page<UserMessageForArticle> getMessageOnArticle(Page<?> userMessageForArticlePage, @Param("uid") String uid);

    @Select("select bu.id as uid,bu.icon,bu.name, bf.id as fid,bf.name as fname,bc.create_time,bc.msg,bf.size as size from blog_file as bf LEFT JOIN blog_user as bu on bu.id=bf.uid LEFT JOIN blog_commit as bc on bc.id=bf.id and bc.type=1 where bu.id=\"${uid}\" and bc.msg is not null")
    Page<UserMessageForFile> getMessageOnFile(Page<?> userMessageForFilePage, @Param("uid") String uid);

    @Select("select ba.id as aid ,ba.title,bu2.id as uid,bu2.name,bu2.icon ,bl.create_time from blog_like as bl LEFT JOIN blog_article as ba on ba.id = bl.like_id LEFT JOIN blog_user as bu on bu.id=ba.uid LEFT JOIN blog_user as bu2 on bu2.id=bl.uid where ba.uid=\"${uid}\"")
    Page<UserMessageForLike> getMessageOnLike(Page<?> userMessageForLikePage, @Param("uid") String uid);

    @Select("select bu.id as uid,bu.name as name,bu.icon,bf.create_time from blog_fans as bf LEFT JOIN blog_user as bu on bu.id=bf.fans_id where bf.uid=\"${uid}\"")
    Page<UserMessageForFans> getMessageOnFans(Page<UserMessageForFans> userMessageForFansPage,@Param("uid") String uid);

    @Select("select count(bc.msg) from blog_user as bu LEFT JOIN blog_article as ba on ba.uid=bu.id left join blog_commit as bc on bc.id=ba.id  where bu.id=\"${uid}\" and bc.type=0 and bc.msg is not null")
    Integer getAllArticleCommits(@Param("uid") Integer uid);

    @Select("select count(bc.msg) from blog_user as bu LEFT JOIN blog_file as bf on bf.uid=bu.id left join blog_commit as bc on bc.id=bf.id  where bu.id=\"${uid}\" and bc.type=1 and bc.msg is not null")
    Integer getAllFileCommits(@Param("uid") Integer uid);
}
