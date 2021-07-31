package org.myblog.blog.controller;


import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.*;

import org.myblog.blog.vo.admin.AdminArticle;
import org.myblog.blog.vo.list.HotArticleList;
import org.myblog.blog.vo.submit.SubmitArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/blog/blog-article")
public class BlogArticleController {

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogArticleService blogArticleService;

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


    BlogHttpUtils blogHttpUtils = new BlogHttpUtils();

    @PostMapping("/save")
    public MsgResult save(@RequestBody SubmitArticle submitArticle, HttpServletResponse response, HttpServletRequest request){
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(cookieUid == null){
            return MsgResult.add(ResultStatus.plz_login);
        }
        String uid = blogRsaService.decrypt(cookieUid);
        if(uid != null) {
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);

            blogTagService.saveTags(submitArticle.getTags().split(","),Integer.valueOf(uid));
            boolean save = blogArticleService.SubmitArticleSave(submitArticle,uid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
           }
            return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/list")
    public MsgResult listOnPage(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        if(currpage != null && pagesize != null){
            List<Map<String, Object>> articles =blogArticleService.pageArticles(Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null ){
                if(articles.size() ==0){
                    return MsgResult.add(ResultStatus.article_commits);
                }
                if(new BlogHttpUtils().getRequestCookieUid(request) != null){
                    new BlogHttpUtils().RefreshCookie(request,response);
                }
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/searchOnPage")
    public MsgResult searchOnPage(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        Object search_str = map.get("search_str");
        if(currpage != null && pagesize != null && search_str!=null){
            List<Map<String, Object>> articles =blogArticleService.SearchPageArticles(search_str.toString(),Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null ){
                if(new BlogHttpUtils().getRequestCookieUid(request) != null){
                    new BlogHttpUtils().RefreshCookie(request,response);
                }
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/searchOnUser")
    public MsgResult searchOnUser(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        Object search_str = map.get("search_str");
        Object uid = map.get("uid");
        if(currpage != null && pagesize != null && search_str!=null && uid != null){
            List<HotArticleList> articles =blogArticleService.SearchOnUserArticles(uid.toString(),search_str.toString(),Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null ){
                if(new BlogHttpUtils().getRequestCookieUid(request) != null){
                    new BlogHttpUtils().RefreshCookie(request,response);
                }
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }

        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/read")
    public MsgResult ReadPage(@RequestBody Map<String,Object> map ,HttpServletRequest request , HttpServletResponse response){
        Object aid = map.get("aid");
        if(aid != null){
            Map<String, Object> map1 = blogArticleService.getReadArticles(aid.toString());
            if(new BlogHttpUtils().getRequestCookieUid(request) != null){
                new BlogHttpUtils().RefreshCookie(request,response);
            }
            return  MsgResult.add(ResultStatus.success).add("data",map1);
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/admin/list")
    public MsgResult adminArticleList(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(currpage != null &&cookieUid!=null && pagesize != null){

            List<HashMap<String, Object>> articles =blogArticleService.listAdminArticle(cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null && articles.size()>0){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/change")
    public MsgResult adminArticleChange(@RequestBody AdminArticle adminArticle, HttpServletResponse response, HttpServletRequest request){
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(adminArticle != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogArticleService.adminArticleChange(adminArticle,cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/del")
    public MsgResult adminArticleDelete(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        Object aids = map.get("aids");
        if(aids != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogArticleService.adminArticleDelete(aids.toString(),cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/search")
    public MsgResult adminArticleSearch(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String s_str = (String) map.get("s_str");
        String s_types = (String) map.get("s_types");
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(s_str != null && s_types != null && currpage != null &&cookieUid!=null && pagesize != null){
            List<HashMap<String, Object>> articles =blogArticleService.searchMultAdminArticle(s_str,s_types, cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

}
