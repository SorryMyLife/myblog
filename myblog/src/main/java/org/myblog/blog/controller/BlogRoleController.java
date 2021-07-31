package org.myblog.blog.controller;


import org.myblog.blog.entity.BlogRole;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogRoleService;

import org.myblog.blog.vo.admin.AdminRole;
import org.myblog.blog.vo.admin.AdminRoleUser;
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
@RequestMapping("/blog/blog-role")
public class BlogRoleController {

    @Autowired
    BlogRoleService blogRoleService;

    @GetMapping("/listrole")
    public MsgResult getRoleList(HttpServletRequest request , HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(cookieUid != null){
           List<BlogRole> blogRoleList = blogRoleService.getRoleList(cookieUid);
           if(blogRoleList != null){
               if(blogRoleList.size() ==0){
                   return MsgResult.add(ResultStatus.article_commits);
               }
               blogHttpUtils.RefreshCookie(request,response);
               return MsgResult.add(ResultStatus.success).add("data",blogRoleList);
           }
        }
        return MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/admin/list")
    public MsgResult adminRoleList(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(currpage != null &&cookieUid!=null && pagesize != null){
            List<HashMap<String, Object>> articles =blogRoleService.listAdminRole(cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                if(articles.size() ==0){
                    return MsgResult.add(ResultStatus.article_commits);
                }
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/change")
    public MsgResult adminRoleChange(@RequestBody AdminRole adminRole, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(adminRole != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogRoleService.adminRoleChange(adminRole,cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/del")
    public MsgResult adminRoleDelete(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        Object rids = map.get("rids");
        if(rids != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogRoleService.adminRoleDelete(rids.toString(),cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/search")
    public MsgResult adminRoleSearch(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String s_str = (String) map.get("s_str");
        String s_types = (String) map.get("s_types");
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(s_str != null && s_types != null && currpage != null &&cookieUid!=null && pagesize != null){
            List<HashMap<String, Object>> articles =blogRoleService.searchMultRole(s_str,s_types, cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/admin/listUser")
    public MsgResult adminRoleUserList(@RequestBody Map<String,Object> map, HttpServletRequest request, HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(currpage != null &&cookieUid!=null && pagesize != null){
            List<HashMap<String, Object>> articles =blogRoleService.listAdminRoleUser(cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/changeUser")
    public MsgResult adminRoleUserChange(@RequestBody AdminRoleUser adminRoleUser, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(adminRoleUser != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogRoleService.adminRoleUserChange(adminRoleUser,cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/delUser")
    public MsgResult adminRoleUserDelete(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        Object ruids = map.get("ruids");
        if(ruids != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            boolean save = blogRoleService.adminRoleUserDelete(ruids.toString(),cookieUid,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/searchUser")
    public MsgResult adminRoleUserSearch(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String s_str = (String) map.get("s_str");
        String s_types = (String) map.get("s_types");
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(s_str != null && s_types != null && currpage != null &&cookieUid!=null && pagesize != null){
            List<HashMap<String, Object>> articles =blogRoleService.searchMultRoleUser(s_str,s_types, cookieUid,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @GetMapping("/getRoleList")
    public MsgResult adminGetUserList(HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String requestCookieUid = blogHttpUtils.getRequestCookieUid(request);
        if (requestCookieUid != null) {
            List<HashMap<String, Object>> userList=blogRoleService.getRoleAdminList(requestCookieUid);
            if(userList != null){
                blogHttpUtils.RefreshCookie(request,response);
                return MsgResult.add(ResultStatus.success).add("data",userList);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }



}
