package org.myblog.blog.controller;


import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.service.BlogLoginService;
import org.myblog.blog.vo.admin.AdminFile;
import org.myblog.blog.vo.admin.AdminLoginList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
 * @since 2021-07-30
 */
@RestController
@RequestMapping("/blog/blog-login")
public class BlogLoginController {

    @Autowired
    BlogLoginService blogLoginService;

    @PostMapping("/admin/list")
    public MsgResult adminFileList(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(currpage != null &&cookieUid!=null && pagesize != null){
            List<HashMap<String, Object>> files =blogLoginService.listAdminLoginList(cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(files != null ){
                if(files.size() ==0){
                    return MsgResult.add(ResultStatus.article_commits);
                }
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",files);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/change")
    public MsgResult adminFileChange(@RequestBody AdminLoginList  adminLoginList, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(adminLoginList != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogLoginService.adminLoginChange(adminLoginList,cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/del")
    public MsgResult adminFileDelete(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        Object lids = map.get("lids");
        if(lids != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogLoginService.adminLoginDelete(lids.toString(),cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/search")
    public MsgResult adminFileSearch(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String s_str = (String) map.get("s_str");
        String s_types = (String) map.get("s_types");
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(s_str != null && s_types != null && currpage != null &&cookieUid!=null && pagesize != null){
            List<HashMap<String, Object>> articles =blogLoginService.searchMultAdminLogin(s_str,s_types, cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }



}
