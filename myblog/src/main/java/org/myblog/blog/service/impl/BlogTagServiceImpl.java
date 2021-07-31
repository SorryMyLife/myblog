package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogArticle;
import org.myblog.blog.entity.BlogCommit;
import org.myblog.blog.entity.BlogFans;
import org.myblog.blog.entity.BlogTag;
import org.myblog.blog.mapper.BlogTagMapper;
import org.myblog.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogArticleService blogArticleService;

    @Autowired
    BlogCommitService blogCommitService;

    @Autowired
    BlogFansService blogFansService;

    @Autowired
    BlogFileService blogFileService;


    @Override
    public List<String> getTags(String tags) {
        String[] split = tags.split(",");
        if(split != null){
            ArrayList<String> strings = new ArrayList<>();
            for (String s : split) {
                BlogTag blogTag = this.getOne(new QueryWrapper<BlogTag>().eq("id", s));
                try {
                    strings.add(blogTag.getName());
                }catch (Exception e){

                }
            }
            return strings;
        }else{
            try {
                ArrayList<String> strings = new ArrayList<>();
                Integer integer = Integer.valueOf(tags);
                BlogTag blogTag = this.getOne(new QueryWrapper<BlogTag>().eq("id", integer));
                strings.add(blogTag.getName());
                return strings;
            }catch (Exception e){
                return null;
            }
        }
    }

    @Override
    public Boolean isExist(String tagname) {
        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.eq("name",tagname);
        return this.getOne(wrapper) != null;
    }

    @Override
    public void saveTags(String[] tags,Integer uid) {

        for (String s : tags) {
            LambdaQueryWrapper<BlogTag> query = Wrappers.<BlogTag>lambdaQuery();
            query.eq(BlogTag::getUid,uid).and(b->{
               b.eq(BlogTag::getName,s);
            });
            BlogTag tag = this.getOne(query);
            if(tag == null){
                BlogTag blogTag = new BlogTag();
                blogTag.setName(s);
                blogTag.setUid(uid);
                this.save(blogTag);
            }

        }

    }

    @Override
    public List<Map<String,Object>> getTagNames(Integer uid) {

        QueryWrapper<BlogTag> query = new QueryWrapper<>();
        query.select("name","count(name) as num").eq("uid",uid).groupBy("name");
        return this.listMaps(query);
    }

    @Override
    public Map<String, Object> getIntoTagPage(String uid, Integer valueOf, Integer valueOf1, Integer valueOf2) {
        Map<String, Object> user = blogUserService.getArticleUser(Integer.valueOf(uid));
        Integer watch = 0;
        List<Map<String, Object>> byTagID = blogArticleService.pageArticlesByTagID(Integer.valueOf(uid), valueOf, valueOf1, valueOf2);
        Page<BlogArticle> articlePage = new Page<>(1,5);
        Page<BlogArticle> blogArticlePage = blogArticleService.page(articlePage, new QueryWrapper<BlogArticle>().eq("uid", uid));
        List<BlogArticle> blogArticles = blogArticlePage.getRecords();
        int fans = blogFansService.count(new QueryWrapper<BlogFans>().eq("uid", uid));
        int commits = blogCommitService.getAllCommits(uid);
        HashMap<String, Object> hashMap = new HashMap<>();
        HashMap<String, Object> usermap = new HashMap<>();
        ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
        QueryWrapper<BlogTag> queryBlogTag = new QueryWrapper<>();
        queryBlogTag.eq("uid",uid);
        List<BlogTag> blogTags = this.list(queryBlogTag);
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
        usermap.put("articles",blogArticlePage.getTotal());
        usermap.put("fans",fans);
        usermap.put("commits",commits);
        hashMap.put("user_info",usermap);
        hashMap.put("tags",blogTags);
        hashMap.put("article",arrayList);
        hashMap.put("articlelist",byTagID);

        return hashMap;
    }

    @Override
    public Integer getTagsByUID(Integer uid) {
        LambdaQueryWrapper<BlogTag> query = Wrappers.<BlogTag>lambdaQuery();
        query.eq(BlogTag::getUid,uid);
        return this.count(query);
    }
}
