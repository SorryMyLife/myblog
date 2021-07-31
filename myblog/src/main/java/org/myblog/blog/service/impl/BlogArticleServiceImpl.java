package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.*;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.mapper.BlogArticleMapper;
import org.myblog.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.myblog.blog.vo.admin.AdminArticle;
import org.myblog.blog.vo.list.HotArticleList;
import org.myblog.blog.vo.submit.SubmitArticle;
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
 * @since 2021-07-17
 */
@Service
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements BlogArticleService {


    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogCommitService blogCommitService;

    @Autowired
    BlogLikeService blogLikeService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogFansService blogFansService;

    @Autowired
    BlogRoleService blogRoleService;

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogAdminUserService blogAdminUserService;

    public String saveTags(Object arr[]){
        return Arrays.toString(arr).replaceAll("\\s+","").replaceAll("\\[|\\]","");
    }

    @Override
    public Boolean SubmitArticleSave(SubmitArticle submitArticle,String uid,LocalDateTime nowTime) {
        BlogArticle blogArticle = new BlogArticle();
        BeanUtils.copyProperties(submitArticle, blogArticle);
        ArrayList<Integer> list = new ArrayList<>();
        for (String TagName : submitArticle.getTags().split(",")) {
            BlogTag blogTag = blogTagService.getOne(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getName, TagName).and(
                    a->{
                        a.eq(BlogTag::getUid,uid);
                    }
            ));
            if(blogTag != null){
                list.add(blogTag.getId());
            }
        }
        blogArticle.setTags(saveTags(list.toArray()));
        blogArticle.setChangeRole(4);
        blogArticle.setChangeTime(nowTime);
        blogArticle.setCreateTime(nowTime);
        blogArticle.setUid(Integer.valueOf(uid));
        blogArticle.setWatch(0);
       return this.save(blogArticle);
    }

    @Override
    public List<Map<String, Object>> pageArticles(IPage<BlogArticle> page){
        List<Map<String, Object>> articles = page.getRecords().stream().map(blogArticle -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            Map<String, Object> blogUser = blogUserService.getArticleUser(blogArticle.getUid());
            HotArticleList hotArticleList = new HotArticleList();
            hashMap.put("user_info",blogUser);
            BeanUtils.copyProperties(blogArticle,hotArticleList);
            hotArticleList.setCommits(blogCommitService.getCommits(blogArticle.getId().toString()));
            hotArticleList.setLikes(blogLikeService.getLikes(blogArticle.getId().toString()));
            hashMap.put("article",hotArticleList);
            hashMap.put("total",page.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return articles;
    }

    @Override
    public List<Map<String, Object>> pageArticles(Integer currentPage, Integer currentSize) {

        IPage<BlogArticle> articleIPage = new Page<>(currentPage, currentSize);
        LambdaQueryWrapper<BlogArticle> queryArticle = Wrappers.<BlogArticle>lambdaQuery();
        queryArticle.select(BlogArticle::getUid,BlogArticle::getId,BlogArticle::getWatch
            ,BlogArticle::getCreateTime,BlogArticle::getTitle, BlogArticle::getHtml, BlogArticle::getText
                , BlogArticle::getText
        );
        IPage<BlogArticle> page = this.page(articleIPage, queryArticle);
        return pageArticles(page);
    }

    @Override
    public List<Map<String, Object>> pageArticlesByUID(IPage<BlogArticle> page){
        List<Map<String, Object>> articles = page.getRecords().stream().map(blogArticle -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            HotArticleList hotArticleList = new HotArticleList();
            BeanUtils.copyProperties(blogArticle,hotArticleList);
            hotArticleList.setCommits(blogCommitService.getCommits(blogArticle.getId().toString()));
            hotArticleList.setLikes(blogLikeService.getLikes(blogArticle.getId().toString()));
            hashMap.put("article",hotArticleList);
            hashMap.put("total",page.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return articles;
    }

    @Override
    public List<Map<String, Object>> pageArticlesByUID(Integer uid, Integer currentPage, Integer currentSize) {
        IPage<BlogArticle> articleIPage = new Page<>(currentPage, currentSize);
        LambdaQueryWrapper<BlogArticle> queryArticle = Wrappers.<BlogArticle>lambdaQuery();
        queryArticle.select(BlogArticle::getUid,BlogArticle::getId,BlogArticle::getWatch
                ,BlogArticle::getCreateTime,BlogArticle::getTitle, BlogArticle::getHtml, BlogArticle::getText
                , BlogArticle::getText
        ).eq(BlogArticle::getUid,uid);
        IPage<BlogArticle> page = this.page(articleIPage, queryArticle);
        return pageArticlesByUID(page);
    }

    @Override
    public List<Map<String, Object>> pageArticlesByTagID(Integer uid, Integer tagid, Integer currentPage, Integer currentSize) {
        IPage<BlogArticle> articleIPage = new Page<>(currentPage, currentSize);
        LambdaQueryWrapper<BlogArticle> queryArticle = Wrappers.<BlogArticle>lambdaQuery();
        queryArticle.select(BlogArticle::getUid,BlogArticle::getId,BlogArticle::getWatch
                ,BlogArticle::getCreateTime,BlogArticle::getTitle, BlogArticle::getHtml, BlogArticle::getText
                , BlogArticle::getText
        ).eq(BlogArticle::getUid,uid).and(a->{
            a.like(BlogArticle::getTags,tagid).or().apply("find_in_set('"+tagid.toString()+"',tags)");
                }
        );
        IPage<BlogArticle> page = this.page(articleIPage, queryArticle);
        return pageArticles(page);
    }

    @Override
    public Map<String, Object> getReadArticles(String aid) {

        BlogArticle blogArticle = this.getOne(new QueryWrapper<BlogArticle>().eq("id", aid));
        if(blogArticle != null){
            Integer uid = blogArticle.getUid();
            Map<String, Object> user = blogUserService.getArticleUser(uid);
            Integer watch = blogArticle.getWatch();
            List<BlogArticle> blogArticles = this.list(new QueryWrapper<BlogArticle>().eq("uid", uid));
            int fans = blogFansService.count(new QueryWrapper<BlogFans>().eq("uid", uid));
            int commits = blogCommitService.count(new QueryWrapper<BlogCommit>().eq("id", aid).and(a -> {
                a.eq("type", 0).or().eq("type", 1);
            }));
            HashMap<String, Object> hashMap = new HashMap<>();
            HashMap<String, Object> usermap = new HashMap<>();
            ArrayList<Map<String,Object>> arrayList = new ArrayList<>();

            QueryWrapper<BlogTag> queryBlogTag = new QueryWrapper<>();
            for (String tagID : blogArticle.getTags().split(",")) {
                queryBlogTag.or().eq("id",tagID);
            }
            queryBlogTag.and(
                    a->{
                        a.eq("uid",uid);
                    }
            );
            List<BlogTag> blogTags = blogTagService.list(queryBlogTag);
            for(BlogArticle b : blogArticles){
                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("id",b.getId());
                map1.put("title",b.getTitle());
                map1.put("watch",b.getWatch());
                arrayList.add(map1);
            }
            usermap.put("icon",user.get("icon"));
            usermap.put("id",user.get("id"));
            usermap.put("name",user.get("name"));
            usermap.put("watchs",watch);
            usermap.put("articles",blogArticles.size());
            usermap.put("fans",fans);
            usermap.put("commits",commits);
            hashMap.put("user_info",usermap);
            hashMap.put("tags",blogTags);
            hashMap.put("article",arrayList);
            hashMap.put("text",blogArticle.getText());
            hashMap.put("html",blogArticle.getHtml());
            hashMap.put("title",blogArticle.getTitle());
            return hashMap;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> SearchPageArticles(String search_str, Integer valueOf, Integer valueOf1) {
        return this.SearchOnUserPageArticles(null,search_str,valueOf,valueOf1);
    }

    @Override
    public List<Map<String, Object>> SearchOnUserPageArticles(String uid, String search_str, Integer valueOf, Integer valueOf1) {
        IPage<BlogArticle> articleIPage = new Page<>(valueOf, valueOf1);
        LambdaQueryWrapper<BlogArticle> queryArticle = Wrappers.<BlogArticle>lambdaQuery();
        queryArticle.select(BlogArticle::getUid,BlogArticle::getId,BlogArticle::getWatch
                ,BlogArticle::getCreateTime,BlogArticle::getTitle, BlogArticle::getHtml, BlogArticle::getText
                , BlogArticle::getText
        ).like(BlogArticle::getText,search_str).or().like(BlogArticle::getTitle,search_str);
        if(uid != null){
            queryArticle.eq(BlogArticle::getUid,uid);
        }
        IPage<BlogArticle> page = this.page(articleIPage, queryArticle);
        return pageArticles(page);
    }

    @Override
    public List<HotArticleList> SearchOnUserArticles(String uid, String search_str, Integer valueOf, Integer valueOf1) {
        IPage<BlogArticle> articleIPage = new Page<>(valueOf, valueOf1);
        LambdaQueryWrapper<BlogArticle> queryArticle = Wrappers.<BlogArticle>lambdaQuery();
        queryArticle.select(BlogArticle::getUid,BlogArticle::getId,BlogArticle::getWatch
                ,BlogArticle::getCreateTime,BlogArticle::getTitle, BlogArticle::getHtml, BlogArticle::getText
                , BlogArticle::getText
        ).like(BlogArticle::getText,search_str).or().like(BlogArticle::getTitle,search_str);
        if(uid != null){
            queryArticle.eq(BlogArticle::getUid,uid);
        }
        IPage<BlogArticle> page = this.page(articleIPage, queryArticle);
        List<HotArticleList> collect = page.getRecords().stream().map(blogArticle -> {
            HotArticleList hotArticleList = new HotArticleList();
            BeanUtils.copyProperties(blogArticle, hotArticleList);
            hotArticleList.setCommits(blogCommitService.getCommits(blogArticle.getId().toString()));
            hotArticleList.setLikes(blogLikeService.getLikes(blogArticle.getId().toString()));
            return hotArticleList;
        }).collect(Collectors.toList());
        return collect;
    }

    public List<HashMap<String, Object>> getAdminArticles(IPage<BlogArticle> blogArticleIPage){
        List<HashMap<String, Object>> collect = blogArticleIPage.getRecords().stream().map(blogArticle -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            AdminArticle adminArticle = new AdminArticle();
            BeanUtils.copyProperties(blogArticle, adminArticle);
            BlogUser user = blogUserService.getUser(adminArticle.getUid().toString());
            adminArticle.setUname(user.getName());
            adminArticle.setCommits(blogCommitService.getCommits(blogArticle.getId().toString()));
            adminArticle.setRoleName(blogRoleService.getRoleName(adminArticle.getChangeRole()));

            List<String> blogTags = blogTagService.getTags(adminArticle.getTags());
            String[] ts = new String[blogTags.size()];
            for (int i = 0; i < blogTags.size(); i++) {
                ts[i] = blogTags.get(i);
            }
            adminArticle.setTagsName(ts);
            hashMap.put("article", adminArticle);
            hashMap.put("total", blogArticleIPage.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HashMap<String, Object>> getAdminArticles(Integer currpage, Integer currsize) {
        IPage<BlogArticle> articleIPage = new Page<>(currpage, currsize);
        IPage<BlogArticle> blogArticleIPage = this.page(articleIPage);
        return getAdminArticles(blogArticleIPage);
    }

    @Override
    public List<HashMap<String, Object>> listAdminArticle(String cookieUid, Integer currpage, Integer currsize) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            return getAdminArticles(currpage, currsize);
        }else{
            IPage<BlogArticle> articleIPage = new Page<>(currpage, currsize);
            IPage<BlogArticle> blogArticleIPage = this.page(articleIPage,new QueryWrapper<BlogArticle>().lambda().eq(BlogArticle::getUid,decrypt));
            return getAdminArticles(blogArticleIPage);
        }
    }

    @Override
    public boolean adminArticleChange(AdminArticle article, String uid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(uid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() <= AdminUserType.user.getId()){
            blogTagService.saveTags(article.getTagsName(),article.getUid());
            BlogArticle blogArticle = new BlogArticle();
            String[] articleTagsName = article.getTagsName();
            String[] tags=new String[articleTagsName.length];
            BeanUtils.copyProperties(article,blogArticle);
            for (int i = 0; i < articleTagsName.length; i++) {
                BlogTag one = blogTagService.getOne(new QueryWrapper<BlogTag>().eq("uid", article.getUid()).eq("name", articleTagsName[i]));
                tags[i]=one.getId().toString();
            }
            blogArticle.setTags(saveTags(tags));
            blogArticle.setChangeRole(adminUser.getRid());
            blogArticle.setChangeTime(nowTime);
            if(blogArticle.getId() != null){
                return this.update(blogArticle,new QueryWrapper<BlogArticle>().eq("id",blogArticle.getId()).eq("uid",blogArticle.getUid()));
            }else{
                blogArticle.setCreateTime(nowTime);
                return this.save(blogArticle);
            }
        }
        return false;
    }

    @Override
    public boolean adminArticleDelete(String aids, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() <= AdminUserType.user.getId()){
            String[] split = aids.split(",");
            if (split != null) {
                for (String u : split) {
                    removeAdminByUID(u);
                }
                return true;
            }else{
                try {
                    Integer value = Integer.valueOf(aids);
                    removeAdminByUID(aids);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }

        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultAdminArticle(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() <= AdminUserType.user.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminArticle(cookieUid, valueOf, valueOf1);
            }
            Page<BlogArticle> blogArticlePage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogArticle> wrapper = new QueryWrapper<>();
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
                   a.lambda().eq(BlogArticle::getUid,decrypt);
                });
            }
            Page<BlogArticle> articlePage = this.page(blogArticlePage, wrapper);
            return getAdminArticles(articlePage);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getTitleList() {
        return this.getBaseMapper().getTitleList();
    }

    private void removeAdminByUID(String u) {
        blogCommitService.remove(new QueryWrapper<BlogCommit>().eq("id",u).eq("type",0));
        this.remove(new QueryWrapper<BlogArticle>().eq("id",u));
        blogLikeService.remove(new QueryWrapper<BlogLike>().eq("like_id",u));
    }
}
