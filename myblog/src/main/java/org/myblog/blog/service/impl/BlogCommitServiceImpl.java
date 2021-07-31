package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.entity.*;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.CommitType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.mapper.BlogCommitMapper;
import org.myblog.blog.service.*;
import org.myblog.blog.vo.admin.AdminCommit;
import org.myblog.blog.vo.user.UserMessageForArticle;
import org.myblog.blog.vo.user.UserMessageForFans;
import org.myblog.blog.vo.user.UserMessageForFile;
import org.myblog.blog.vo.user.UserMessageForLike;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
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
public class BlogCommitServiceImpl extends ServiceImpl<BlogCommitMapper, BlogCommit> implements BlogCommitService {

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogRoleService blogRoleService;

    @Autowired
    BlogFileService blogFileService;

    @Autowired
    BlogArticleService blogArticleService;

    @Override
    public Integer getCommits(String articleid) {
        LambdaQueryWrapper<BlogCommit> lambdaQuery = Wrappers.<BlogCommit>lambdaQuery();
        lambdaQuery.eq(BlogCommit::getId,articleid).and(a->{
            a.eq(BlogCommit::getType,0);
        });
        return this.count(lambdaQuery);
    }

    @Override
    public Integer getFileCommits(String fid) {
        LambdaQueryWrapper<BlogCommit> lambdaQuery = Wrappers.<BlogCommit>lambdaQuery();
        lambdaQuery.eq(BlogCommit::getId,fid).and(a->{
            a.eq(BlogCommit::getType,1);
        });
        return this.count(lambdaQuery);
    }

    @Override
    public Integer getAllCommits(String uid) {
        Integer aid_commits=this.getBaseMapper().getAllArticleCommits(Integer.valueOf(uid));
        Integer fid_commits=this.getBaseMapper().getAllFileCommits(Integer.valueOf(uid));
        return aid_commits+fid_commits;
    }

    @Override
    public Page<UserMessageForArticle> getMessageOnArticle(Integer valueOf, Integer valueOf1, String uid) {
        Page<UserMessageForArticle> userMessageForArticlePage = new Page<>(valueOf,valueOf1);
        return this.getBaseMapper().getMessageOnArticle(userMessageForArticlePage,uid);

    }

    @Override
    public Page<UserMessageForFile> getMessageOnFile(Integer valueOf, Integer valueOf1, String uid) {
        Page<UserMessageForFile> userMessageForFilePage = new Page<>(valueOf,valueOf1);
        return this.getBaseMapper().getMessageOnFile(userMessageForFilePage,uid);
    }

    @Override
    public Page<UserMessageForLike> getMessageOnLike(Integer valueOf, Integer valueOf1, String uid) {
        Page<UserMessageForLike> UserMessageForLikePage = new Page<>(valueOf,valueOf1);
        return this.getBaseMapper().getMessageOnLike(UserMessageForLikePage,uid);
    }

    @Override
    public Page<UserMessageForFans> getMessageOnFans(Integer valueOf, Integer valueOf1, String uid) {
        Page<UserMessageForFans> userMessageForFansPage = new Page<>(valueOf,valueOf1);
        return this.getBaseMapper().getMessageOnFans(userMessageForFansPage,uid);
    }

    @Override
    public List<HashMap<String, Object>> listAdminCommit(String cookieUid, Integer valueOf, Integer valueOf1) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            return getAdminCommit(valueOf,valueOf1);
        }else{
            Page<BlogCommit> blogCommitPage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogCommit> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(BlogCommit::getUid,cookieUid);
            Page<BlogCommit> commitPage = this.page(blogCommitPage,wrapper);
            return getAdminCommit(commitPage);
        }
    }

    @Override
    public List<HashMap<String, Object>> getAdminCommit(Page<BlogCommit> commitPage){
        List<HashMap<String, Object>> collect = commitPage.getRecords().stream().map(blogCommit -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            AdminCommit adminCommit = new AdminCommit();
            BeanUtils.copyProperties(blogCommit, adminCommit);
            BlogUser user = blogUserService.getUser(adminCommit.getUid().toString());
            adminCommit.setUname(user.getName());
            adminCommit.setRoleName(blogRoleService.getRoleName(adminCommit.getChangeRole()));
            if(blogCommit.getTags().equals("0")){
                adminCommit.setTagsName(new String[]{""});
            }else{
                List<String> blogTags = blogTagService.getTags(adminCommit.getTags());
                String[] ts = new String[blogTags.size()];
                for (int i = 0; i < blogTags.size(); i++) {
                    ts[i] = blogTags.get(i);
                }
                adminCommit.setTagsName(ts);
            }
            adminCommit.setTypeName(blogCommit.getType()==0?"文章":"文件");
            switch (CommitType.getByID(blogCommit.getType())){
                case file:
                    BlogFile file = blogFileService.getFile(blogCommit.getId());
                    if(file != null){
                        adminCommit.setTitle(file.getName());
                    }
                    break;
                case article:
                    BlogArticle blogArticle = blogArticleService.getOne(new QueryWrapper<BlogArticle>().eq("id", blogCommit.getId()));
                    if(blogArticle != null){
                        adminCommit.setTitle(blogArticle.getTitle());
                    }
                    break;
            }
            hashMap.put("commit", adminCommit);
            hashMap.put("total", commitPage.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HashMap<String, Object>> getAdminCommit(Integer valueOf, Integer valueOf1) {
        Page<BlogCommit> blogCommitPage = new Page<>(valueOf, valueOf1);
        Page<BlogCommit> commitPage = this.page(blogCommitPage);
        return getAdminCommit(commitPage);
    }

    @Override
    public List<Map<String, Object>> getTitleList(String decrypt, Integer c_type) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            switch (CommitType.getByID(c_type)){
                case file:
                    return blogFileService.getTitleList();
                case article:
                    return blogArticleService.getTitleList();
            }
        }
        return null;
    }

    public String saveTags(Object arr[]){
        return Arrays.toString(arr).replaceAll("\\s+","").replaceAll("\\[|\\]","");
    }

    @Override
    public boolean adminCommitChange(AdminCommit adminCommit, String cookieUid, LocalDateTime nowTime) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if(adminUser != null && adminUser.getRid() <= AdminUserType.user.getId()){
            blogTagService.saveTags(adminCommit.getTagsName(),adminCommit.getUid());
            BlogCommit blogCommit = new BlogCommit();
            String[] articleTagsName = adminCommit.getTagsName();
            String[] tags=new String[articleTagsName.length];
            BeanUtils.copyProperties(adminCommit,blogCommit);
            for (int i = 0; i < articleTagsName.length; i++) {
                BlogTag one = blogTagService.getOne(new QueryWrapper<BlogTag>().eq("uid", adminCommit.getUid()).eq("name", articleTagsName[i]));
                tags[i]=one.getId().toString();
            }
            blogCommit.setTags(saveTags(tags));
            blogCommit.setChangeRole(adminUser.getRid());
            blogCommit.setChagneTime(nowTime);
            if(blogCommit.getCreateTime() != null){
                return this.update(blogCommit,new QueryWrapper<BlogCommit>().eq("id",blogCommit.getId()).eq("uid",blogCommit.getUid()).eq("create_time",blogCommit.getCreateTime()));
            }else{
                blogCommit.setCreateTime(nowTime);
                return this.save(blogCommit);
            }
        }
        return false;
    }

    private void removeAdminByUID(String u) {
       this.remove(new QueryWrapper<BlogCommit>().eq("id",u));
    }

    @Override
    public boolean adminCommitDelete(String cids, String cookieUid, LocalDateTime nowTime) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if(adminUser != null && adminUser.getRid() <= AdminUserType.user.getId()){
            String[] split = cids.split(",");
            if (split != null) {
                for (String u : split) {
                    removeAdminByUID(u);
                }
                return true;
            }else{
                try {
                    Integer value = Integer.valueOf(cids);
                    removeAdminByUID(cids);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }

        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultAdminCommit(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminCommit(cookieUid, valueOf, valueOf1);
            }
            Page<BlogCommit> blogCommitPage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogCommit> wrapper = new QueryWrapper<>();
            String[] split = s_types.split(",");
            if(split != null){
                for (String s : split) {
                    String byCloum = searchMultType.getByCloum(s);
                    if(byCloum!=null){
                        wrapper.or().like(byCloum,s_str);
                    }
                }
            }else{
                String byCloum = searchMultType.getByCloum(s_types);
                if(byCloum!=null){
                    wrapper.like(byCloum,s_str);
                }
            }
            if(adminUser.getRid() == AdminUserType.user.getId()){
                wrapper.and(a->{
                    a.lambda().eq(BlogCommit::getUid,cookieUid);
                });
            }
            Page<BlogCommit> commitPage = this.page(blogCommitPage, wrapper);
           return getAdminCommit(commitPage);
        }
        return null;
    }
}
