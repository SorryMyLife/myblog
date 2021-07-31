package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.*;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.LoginType;
import org.myblog.blog.extend.utils.pwdUtils;
import org.myblog.blog.mapper.BlogUserMapper;
import org.myblog.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.vo.list.FileList;
import org.myblog.blog.vo.list.HotArticleList;
import org.myblog.blog.vo.LoginUser;
import org.myblog.blog.vo.user.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Service
public class BlogUserServiceImpl extends ServiceImpl<BlogUserMapper, BlogUser> implements BlogUserService {

    @Autowired
    BlogArticleService blogArticleService;
    @Autowired
    BlogCommitService blogCommitService;
    @Autowired
    BlogFansService blogFansService;
    @Autowired
    BlogLikeService blogLikeService;
    @Autowired
    BlogFileService blogFileService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Autowired
    BlogCodeService blogCodeService;

    @Override
    public Integer getUid() {
        return uid;
    }


    public Integer uid;

    @Override
    public BlogUser isLogin(LoginUser loginUser) {
        if(loginUser.getPassword().length() < 6){
            return null;
        }
        String userId = loginUser.getId();
        Integer type = loginUser.getLoginType();
        String code = loginUser.getCode();
        BlogCode blogCode = blogCodeService.getBlogCode(userId, code);
        if(blogCode != null){
            LambdaQueryWrapper<BlogUser> blogUserLambdaQueryWrapper = Wrappers.<BlogUser>lambdaQuery();
            switch (LoginType.getByID(type)){
                case uid:
                    blogUserLambdaQueryWrapper.eq(BlogUser::getId,userId);
                    break;
                case email:
                    blogUserLambdaQueryWrapper.eq(BlogUser::getId,userId);
                    break;
                case phone:
                    blogUserLambdaQueryWrapper.eq(BlogUser::getPhone,userId);
                    break;
            }
            blogUserLambdaQueryWrapper.and(a->{
                a.eq(BlogUser::getPassword,loginUser.getPassword())
                        .or().
                        eq(BlogUser::getPassword,pwdUtils.enpwd(loginUser.getPassword()));
            });
            BlogUser blogUser = this.getBaseMapper().selectOne(blogUserLambdaQueryWrapper);
            return blogUser;
        }
        return null;
    }

    @Override
    public UserInfo getUserInfo(BlogUser blogUser) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(blogUser,userInfo);

        userInfo.setId(blogUser.getId().toString());
        userInfo.setArticles(blogArticleService.list(new QueryWrapper<BlogArticle>().eq("uid",blogUser.getId())).size());
        userInfo.setCommits(blogCommitService.list(new QueryWrapper<BlogCommit>().eq("uid",blogUser.getId())).size());
        userInfo.setFans(blogFansService.list(new QueryWrapper<BlogFans>().eq("uid",blogUser.getId())).size());
        userInfo.setFiles(blogFileService.list(new QueryWrapper<BlogFile>().eq("uid",blogUser.getId())).size());
        userInfo.setLikes(blogLikeService.list(new QueryWrapper<BlogLike>().eq("uid",blogUser.getId())).size());
        userInfo.setWatch(blogUser.getWatch()==null?0:blogUser.getWatch());
        return userInfo;
    }

    @Override
    public UserInfo getUserInfo(String uid) {
        return getUserInfo(getUser(uid));
    }

    @Override
    public BlogUser getUser(String uid) {
        QueryWrapper<BlogUser> wrapper = new QueryWrapper<>();
        wrapper.select(BlogUser.class,b-> !b.getColumn().equals("password")).eq("id",uid);
        return this.getOne(wrapper);
    }

    @Override
    public Boolean regUser(LoginUser loginUser) {
        if(loginUser.getPassword().length() < 6){
            return false;
        }
        LocalDateTime nowTime = LocalDateTime.now().withNano(0);
        BlogAdminUser blogAdminUser = new BlogAdminUser();
        blogAdminUser.setChangeRole(AdminUserType.system.getId());
        blogAdminUser.setCreateRole(AdminUserType.system.getId());
        blogAdminUser.setChangeTime(nowTime);
        blogAdminUser.setCreateTime(nowTime);
        blogAdminUser.setRid(AdminUserType.user.getId());
        BlogUser blogUser = new BlogUser();
        blogUser.setName(loginUser.getId());
        blogUser.setEmail(loginUser.getId());
        blogUser.setPhone(loginUser.getId());
        blogUser.setPassword(pwdUtils.enpwd(loginUser.getPassword()));
        blogUser.setLoginType(LoginType.uid.getLogincode());
        blogUser.setCreateRole(AdminUserType.system.getId());
        blogUser.setChangeTime(nowTime);
        blogUser.setCreateTime(nowTime);
        blogUser.setChangeRole(AdminUserType.system.getId());
        blogUser.setIcon("data:image/jpg;base64,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");
        boolean save = this.save(blogUser);
        if(save){
            QueryWrapper<BlogUser> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(BlogUser::getName,blogUser.getName())
            .eq(BlogUser::getEmail,blogUser.getEmail()).eq(BlogUser::getPhone,blogUser.getPhone())
            .eq(BlogUser::getCreateTime,blogUser.getCreateTime());
            BlogUser one = this.getOne(wrapper);
            if(one != null){
                blogAdminUser.setUid(one.getId());
                this.uid=one.getId();
                return blogAdminUserService.save(blogAdminUser);
            }
        }
        return false;
    }

    @Override
    public HashMap<String, Object> getUserPageInfo(String uid,Integer currentpage,Integer currentsize) {
        HashMap<String, Object> userInfoPageMap = new HashMap<>();
        UserInfo userInfo = this.getUserInfo(uid);
        IPage<BlogArticle> articleIPage = new Page<>(currentpage, currentsize);
        IPage<BlogArticle> blogArticleIPage = blogArticleService.
                page(articleIPage, new QueryWrapper<BlogArticle>().eq("uid", uid));
        Page<Map<String,Object>> blogFilePage = new Page<>(currentpage, currentsize);
        QueryWrapper<BlogFile> blogFileQueryWrapper = new QueryWrapper<>();
        blogFileQueryWrapper.select(BlogFile.class,b->{
            return !b.getColumn().equals("change_role") && !b.getColumn().equals("change_time");
        }).eq("uid", uid);
        Page<FileList> blogFilePageMap = blogFileService.getFileByUID(blogFilePage, Integer.valueOf(uid));
        List<FileList> filePageMapRecords = blogFilePageMap.getRecords();
        List<FileList> fileList2 = filePageMapRecords.stream().map(fileList1 -> {
            int commits = blogCommitService.count(new QueryWrapper<BlogCommit>().eq("id", fileList1.getFid()).and(a -> {
                a.eq("type", 1);
            }));
            fileList1.setCommits(commits);
            return fileList1;
        }).collect(Collectors.toList());
        List<HotArticleList> hotArticleLists =  blogArticleIPage.getRecords().stream().map(blogArticle -> {
            HotArticleList hotArticleList = new HotArticleList();
            BeanUtils.copyProperties(blogArticle, hotArticleList);
            hotArticleList.setCommits(blogCommitService.getCommits(blogArticle.getId().toString()));
            hotArticleList.setLikes(blogLikeService.getLikes(blogArticle.getId().toString()));
            return hotArticleList;
        }).collect(Collectors.toList());
        List<Integer> integerList = hotArticleLists.stream().map(a -> {
            return Integer.valueOf(a.getCommits());
        }).collect(Collectors.toList());
        Integer commits=integerList.stream().reduce(Integer::sum).get();
        List<BlogFans> blogFans = blogFansService.list(new QueryWrapper<BlogFans>().eq("uid", userInfo.getId()));
        List<BlogTag> blogTags = blogTagService.list(new QueryWrapper<BlogTag>().eq("uid", uid));
        userInfoPageMap.put("user_info",userInfo);
        userInfoPageMap.put("article_total",blogArticleIPage.getTotal());
        userInfoPageMap.put("articles",hotArticleLists);
        userInfoPageMap.put("file_total",blogFilePageMap.getTotal());
        userInfoPageMap.put("files",fileList2);
        userInfoPageMap.put("tags",blogTags);
        userInfoPageMap.put("commits",commits);
        userInfoPageMap.put("fans",blogFans.size());
        userInfoPageMap.put("watchs",userInfo.getWatch());

        return userInfoPageMap;
    }

    @Override
    public Map<String, Object> getArticleUser(Integer uid) {
        LambdaQueryWrapper<BlogUser> query = Wrappers.<BlogUser>lambdaQuery();
        query.select(BlogUser::getId,BlogUser::getIcon,BlogUser::getName).eq(BlogUser::getId,uid);

        return this.getMap(query);
    }



}
