package org.myblog.blog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.entity.*;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.extend.utils.pwdUtils;
import org.myblog.blog.mapper.BlogAdminUserMapper;
import org.myblog.blog.service.*;
import org.myblog.blog.vo.admin.AdminPwd;
import org.myblog.blog.vo.admin.AdminUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-24
 */
@Service
public class BlogAdminUserServiceImpl extends ServiceImpl<BlogAdminUserMapper, BlogAdminUser> implements BlogAdminUserService {

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogRoleService blogRoleService;

    @Autowired
    BlogFileService blogFileService;

    @Autowired
    BlogFansService blogFansService;

    @Autowired
    BlogCommitService blogCommitService;

    @Autowired
    BlogArticleService blogArticleService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogLikeService blogLikeService;

    @Autowired
    AdministrativeDivisionCodeService administrativeDivisionCodeService;

    @Override
    public BlogAdminUser getAdminUser(Integer uid) {
        BlogUser user = blogUserService.getUser(uid.toString());
        if(user != null){
            return this.getOne(new QueryWrapper<BlogAdminUser>().eq("uid",user.getId()));
        }
        return null;
    }

    public void getAdminUser(HashMap<String, Object> hashMap,BlogUser blogUser){
        AdminUser adminUser1 = new AdminUser();
        BlogAdminUser blogAdminUser = this.getAdminUser(blogUser.getId());
        BeanUtils.copyProperties(blogUser,adminUser1);
        if(blogUser.getAddress() != null){
            adminUser1.setAddr(blogUser.getAddress().split(","));
            adminUser1.setAddress2(administrativeDivisionCodeService.getDivisionName(blogUser.getAddress()));
        }
        adminUser1.setRole_name(blogAdminUser.getRid());
        adminUser1.setRoleName(blogRoleService.getRoleName(blogUser.getChangeRole()));
        adminUser1.setCroleName(blogRoleService.getRoleName(blogUser.getCreateRole()));
        hashMap.put("user_info",adminUser1);
        hashMap.put("articles",blogArticleService.count(new QueryWrapper<BlogArticle>().eq("uid",blogUser.getId())));
        hashMap.put("role_name",blogRoleService.getRoleName(blogAdminUser.getRid()));
        hashMap.put("files",blogFileService.getFilesByUID(blogUser.getId()));
        hashMap.put("tags",blogTagService.getTagsByUID(blogUser.getId()));
        hashMap.put("fans",blogFansService.getFansByUID(blogUser.getId()));
        hashMap.put("commits",blogCommitService.getAllCommits(blogUser.getId().toString()));
    }

    @Override
    public List<HashMap<String, Object>> getAdminUsers(Page<BlogUser> userPage){
        List<HashMap<String, Object>> collect = userPage.getRecords().stream().map(blogUser -> {
//                    Logger.getLogger("blogUser").info(blogUser.toString());
            HashMap<String, Object> hashMap = new HashMap<>();
            getAdminUser(hashMap,blogUser);
            hashMap.put("total",userPage.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HashMap<String, Object>> getAdminUsers(Integer currentpage, Integer currentsize) {
        Page<BlogUser> blogUserPage = new Page<>(currentpage,currentsize);
        QueryWrapper<BlogUser> wrapper = new QueryWrapper<>();
        wrapper.select(BlogUser.class,b-> !b.getColumn().equals("password"));
        Page<BlogUser> userPage = blogUserService.page(blogUserPage, wrapper);
        return getAdminUsers(userPage);
    }

    @Override
    public List<HashMap<String, Object>> listAdminUsers(Integer currentpage,Integer currentsize,Integer uid) {
        BlogAdminUser adminUser = this.getAdminUser(uid);
        if (adminUser != null ) {
            if(adminUser.getRid() < AdminUserType.user.getId()){
                return getAdminUsers(currentpage, currentsize);
            }else{
                List<HashMap<String, Object>> list =new ArrayList<>();
                HashMap<String, Object> hashMap = new HashMap<>();
                BlogUser blogUser = blogUserService.getUser(adminUser.getUid().toString());
                getAdminUser(hashMap,blogUser);
                hashMap.put("total","1");
                list.add(hashMap);
                return list;
            }

        }

        return null;
    }

    @Override
    public Boolean changeAdminUser(Integer uid, AdminUser adminUser) {
        BlogAdminUser blogAdminUser = this.getAdminUser(uid);
        if(blogAdminUser != null ){
            if(blogAdminUser.getRid() < AdminUserType.user.getId()){
                BlogUser blogUser = new BlogUser();
                LocalDateTime time = LocalDateTime.now().withNano(0);
                BeanUtils.copyProperties(adminUser,blogUser);
                blogUser.setAddress(Arrays.toString(adminUser.getAddr()).replaceAll("\\[|\\]|\\s+",""));
                blogUser.setChangeRole(blogAdminUser.getRid());
                blogUser.setChangeTime(time);
                if(blogUser.getId() != null){
                    return blogUserService.update(blogUser,new QueryWrapper<BlogUser>().eq("id",adminUser.getId()));
                }else{
                    blogUser.setCreateTime(time);
                    blogUser.setCreateRole(blogAdminUser.getRid());
                    if(blogUserService.save(blogUser)){
                        LocalDateTime time2 = LocalDateTime.now().withNano(0);
                        LambdaQueryWrapper<BlogUser> query = Wrappers.<BlogUser>lambdaQuery();
                        query.eq(BlogUser::getCreateRole,blogAdminUser.getRid()).eq(BlogUser::getCreateTime,time);
                        BlogAdminUser blogAdminUser1 = new BlogAdminUser();
                        blogAdminUser1.setRid(adminUser.getRole_name());
                        blogAdminUser1.setUid(blogUserService.getOne(query).getId());
                        blogAdminUser1.setCreateTime(time2);
                        blogAdminUser1.setChangeTime(time2);
                        blogAdminUser1.setCreateRole(blogAdminUser.getRid());
                        blogAdminUser1.setChangeRole(blogAdminUser.getRid());
                        return this.save(blogAdminUser1);
                    }
                }
            }else{
                BlogUser blogUser = new BlogUser();
                LocalDateTime time = LocalDateTime.now().withNano(0);
                BeanUtils.copyProperties(adminUser,blogUser);
                blogUser.setAddress(Arrays.toString(adminUser.getAddr()).replaceAll("\\[|\\]|\\s+",""));
                blogUser.setChangeRole(blogAdminUser.getRid());
                blogUser.setChangeTime(time);
                if(blogUser.getId() != null){
                    return blogUserService.update(blogUser,new QueryWrapper<BlogUser>().eq("id",adminUser.getId()));
                }else {
                    blogUser.setCreateTime(time);
                    blogUser.setCreateRole(blogAdminUser.getRid());
                    blogUserService.save(blogUser);
                }
            }

        }
        return false;
    }

    @Override
    public Boolean changeAdminPwd(Integer uid, AdminPwd adminPwd) {
        BlogAdminUser blogAdminUser = this.getAdminUser(uid);
        if(blogAdminUser != null ){
            if( blogAdminUser.getRid() <= AdminUserType.user.getId()){
                LambdaQueryWrapper<BlogUser> query = Wrappers.<BlogUser>lambdaQuery();
                query.eq(BlogUser::getId,adminPwd.getUid());
                BlogUser userServiceOne = blogUserService.getOne(query);
                if(userServiceOne != null){
                    String enpwd = pwdUtils.enpwd(adminPwd.getNewpwd2());
                    if(blogAdminUser.getRid()==0){
                        userServiceOne.setPassword(enpwd);
                        return blogUserService.update(userServiceOne,query);
                    }else{
                        if(adminPwd.getNewpwd1().equals(adminPwd.getNewpwd2()) && !adminPwd.getNewpwd1().equals(adminPwd.getOldpwd())){
                            userServiceOne.setPassword(enpwd);
                            return blogUserService.update(userServiceOne,query);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Boolean delAdminUsers(Integer uid, String uids) {
        BlogAdminUser blogAdminUser = this.getAdminUser(uid);
        if(blogAdminUser != null && blogAdminUser.getRid() < AdminUserType.user.getId()){
            String[] split = uids.split(",");
            if (split != null) {
                for (String u : split) {
                    removeAdminByUID(u);
                }
                return true;
            }else{
                try {
                    Integer value = Integer.valueOf(uids);
                    removeAdminByUID(uids);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void removeAdminByUID(String u) {
        this.remove(new QueryWrapper<BlogAdminUser>().eq("uid",u));
        blogTagService.remove(new QueryWrapper<BlogTag>().eq("uid",u));
        blogLikeService.remove(new QueryWrapper<BlogLike>().eq("uid",u));
        blogFansService.remove(new QueryWrapper<BlogFans>().eq("uid",u));
        blogCommitService.remove(new QueryWrapper<BlogCommit>().eq("uid",u));
        List<BlogFile> blogFiles = blogFileService.list(new QueryWrapper<BlogFile>().eq("uid", u));
        List<BlogArticle> articles = blogArticleService.list(new QueryWrapper<BlogArticle>().eq("uid", u));
        for (BlogArticle article : articles) {
            blogCommitService.remove(new QueryWrapper<BlogCommit>().eq("id",article.getId()).and(a->{
                a.eq("type",0);
            }));
        }
        for (BlogFile file : blogFiles) {
            blogCommitService.remove(new QueryWrapper<BlogCommit>().eq("id",file.getId()).and(a->{
                a.eq("type",1);
            }));
        }
        blogFileService.remove(new QueryWrapper<BlogFile>().eq("uid", u));
        blogArticleService.remove(new QueryWrapper<BlogArticle>().eq("uid", u));
        blogUserService.remove(new QueryWrapper<BlogUser>().eq("id", u));
    }

    @Override
    public List<HashMap<String, Object>> searchMultAdminUsers(String s_types, String s_str, Integer currentpage, Integer currentsize, Integer uid) {
        BlogAdminUser adminUser = this.getAdminUser(uid);
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            if(s_types.equals("") && s_str.equals("")){
                return listAdminUsers(currentpage,currentsize,uid);
            }
            Page<BlogUser> blogUserPage = new Page<>(currentpage,currentsize);
            QueryWrapper<BlogUser> wrapper = new QueryWrapper<>();
            wrapper.select(BlogUser.class,b-> !b.getColumn().equals("password"));
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
            Page<BlogUser> userPage = blogUserService.page(blogUserPage, wrapper);
            return getAdminUsers(userPage);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getUserList(String requestCookieUid) {
        BlogAdminUser adminUser = this.getAdminUser(Integer.valueOf(requestCookieUid));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            LambdaQueryWrapper<BlogUser> query = Wrappers.<BlogUser>lambdaQuery();
            query.select(BlogUser::getId,BlogUser::getName);
            List<Map<String, Object>> collect = blogUserService.list(query).stream().map(blogUser -> {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("id",blogUser.getId());
                hashMap.put("name",blogUser.getName());
                return hashMap;
            }).collect(Collectors.toList());
            return collect;

        }
        return null;
    }
}
