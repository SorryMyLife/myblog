package org.myblog.blog.controller;


import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogAdminUserService;
import org.myblog.blog.service.BlogRsaService;

import org.myblog.blog.vo.admin.AdminPwd;
import org.myblog.blog.vo.admin.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author test
 * @since 2021-07-24
 */
@RestController
@RequestMapping("/blog/blog-admin-user")
public class BlogAdminUserController {

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @PostMapping("/listuser")
    public MsgResult adminListUser(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String uid = blogHttpUtils.getRequestCookieUid(request);
        Object currentpage = map.get("currentpage");
        Object currentsize = map.get("currentsize");
        if(uid != null && currentpage != null && currentsize != null){
            String decrypt = blogRsaService.decrypt(uid);
            List<HashMap<String, Object>> blogUsers = blogAdminUserService.listAdminUsers(Integer.valueOf(currentpage.toString()),Integer.valueOf(currentsize.toString()), Integer.valueOf(decrypt));
            if(blogUsers != null){
                if(blogUsers.size() ==0){
                    return MsgResult.add(ResultStatus.article_commits);
                }
                return MsgResult.add(ResultStatus.success).add("data",blogUsers);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/changeuser")
    public MsgResult adminChangeUser(@RequestBody AdminUser adminUser, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String uid = blogHttpUtils.getRequestCookieUid(request);
        if(uid != null &&adminUser!=null){
            String decrypt = blogRsaService.decrypt(uid);
            Boolean isChange = blogAdminUserService.changeAdminUser(Integer.valueOf(decrypt),adminUser);
            if(isChange){
                blogHttpUtils.RefreshCookie(request,response);
                return MsgResult.add(ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/changepwd")
    public MsgResult adminChangePwd(@RequestBody AdminPwd adminPwd, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String uid = blogHttpUtils.getRequestCookieUid(request);
        if(uid != null && adminPwd != null){
            String decrypt = blogRsaService.decrypt(uid);
            Boolean isChange = blogAdminUserService.changeAdminPwd(Integer.valueOf(decrypt),adminPwd);
            if(isChange){
                blogHttpUtils.RefreshCookie(request,response);
                return MsgResult.add(ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.pwd_change_error);
    }

    @PostMapping("/delUser")
    public MsgResult adminDelUser(@RequestBody Map<String,Object> map ,HttpServletResponse response,HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String uids = (String) map.get("uids");
        String uid = blogHttpUtils.getRequestCookieUid(request);
        if(uid != null && uids != null){
            String decrypt = blogRsaService.decrypt(uid);
            Boolean isChange = blogAdminUserService.delAdminUsers(Integer.valueOf(decrypt),uids);
            if(isChange){
                blogHttpUtils.RefreshCookie(request,response);
                return MsgResult.add(ResultStatus.success);
            }
        }
        return MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/searchUser")
    public MsgResult adminSearchUser(@RequestBody Map<String,Object> map ,HttpServletResponse response,HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String s_str = (String) map.get("s_str");
        String s_types = (String) map.get("s_types");
        String uid = blogHttpUtils.getRequestCookieUid(request);
        Integer currentpage = (Integer) map.get("currentpage");
        Integer currentsize = (Integer) map.get("currentsize");
        if(uid != null && currentpage != null&& s_str != null&& s_types != null && currentsize != null){
            String decrypt = blogRsaService.decrypt(uid);
            try {
                List<HashMap<String, Object>> blogUsers = blogAdminUserService.searchMultAdminUsers(s_types,s_str, currentpage,currentsize, Integer.valueOf(decrypt));
                if(blogUsers != null){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success).add("data",blogUsers);
                }
            }catch (Exception e){
                return MsgResult.add(ResultStatus.admin_search_error);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @GetMapping("/getUserList")
    public MsgResult adminGetUserList(HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String requestCookieUid = blogHttpUtils.getRequestCookieUid(request);
        if (requestCookieUid != null) {
            String decrypt = blogRsaService.decrypt(requestCookieUid);
            List<Map<String,Object>> userList=blogAdminUserService.getUserList(decrypt);
            if(userList != null){
                return MsgResult.add(ResultStatus.success).add("data",userList);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }




}
