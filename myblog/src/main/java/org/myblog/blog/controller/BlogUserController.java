package org.myblog.blog.controller;


import org.myblog.blog.entity.BlogUser;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogArticleService;
import org.myblog.blog.service.BlogLoginService;
import org.myblog.blog.service.BlogRsaService;
import org.myblog.blog.service.BlogUserService;
import org.myblog.blog.vo.LoginUser;
import org.myblog.blog.vo.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
@RequestMapping("/blog/blog-user")
public class BlogUserController {

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogArticleService blogArticleService;

    @Autowired
    BlogLoginService blogLoginService;


    @PostMapping("/login")
    public Object login(HttpServletResponse response,HttpServletRequest request, @RequestBody LoginUser loginUser){
        BlogUser blogUser = blogUserService.isLogin(loginUser);
        Integer errorCount = blogLoginService.getErrorCount(loginUser.getId());
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String ipAddress = blogHttpUtils.getIpAddress(request);
        if(ipAddress == null || ipAddress.isEmpty()){
            ipAddress="未知";
        }
        if(blogUser != null){
            blogLoginService.saveOK(blogUser.getId().toString(),ipAddress);
            String encrypt = blogRsaService.encrypt(blogUser.getId().toString());
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",encrypt);
            blogHttpUtils.addCookie(hashMap,response);
            return MsgResult.add(ResultStatus.success);
        }
        if(errorCount >= 3){
            return MsgResult.add(ResultStatus.login_max);
        }
        blogLoginService.saveError(loginUser.getId(),ipAddress);
        return MsgResult.add(ResultStatus.login_failure);
    }

    @PostMapping("/reg")
    public MsgResult reg(@RequestBody LoginUser loginUser){
        BlogUser user = blogUserService.getUser(loginUser.getId());
        if(user == null){
            Boolean aBoolean = blogUserService.regUser(loginUser);
            if(aBoolean){
                return MsgResult.add(ResultStatus.success).add("data","你的uid为: "+blogUserService.getUid());
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }


    @GetMapping("/logout")
    public MsgResult logout(HttpServletResponse response,HttpServletRequest request){
        new BlogHttpUtils().loginout(request,response);
        return MsgResult.add(ResultStatus.logout);
    }


    @GetMapping("/getUserInfo")
    public Object getUserInfo(HttpServletRequest request , HttpServletResponse response){
        if(request.getCookies() != null){
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("uid")){
                    String decrypt = blogRsaService.decrypt(cookie.getValue());
                    if(Integer.valueOf(decrypt) > 0){
                        UserInfo userInfo = blogUserService.getUserInfo(decrypt);
                        if(userInfo != null){
                            new BlogHttpUtils().RefreshCookie(request,response);
                            return MsgResult.add(ResultStatus.success).add("user_info",userInfo);
                        }
                    }
                }
            }
        }
        return MsgResult.add(ResultStatus.failure);
    }


    @PostMapping("/getUserPageInfo")
    public MsgResult getUserPageInfo(@RequestBody Map<String,Object> map,HttpServletResponse response,HttpServletRequest request){
        try {
            Object uid = map.get("uid");
            Object currentpage = map.get("currentpage");
            Object currentsize = map.get("currentsize");
            if(uid != null){
                if(uid.equals("user")){
                    for (Cookie cookie : request.getCookies()) {
                        if (cookie.getName().equals("uid")) {
                            uid=blogRsaService.decrypt(cookie.getValue());
                        }
                    }
                }
                HashMap<String, Object> userPageInfo = blogUserService.getUserPageInfo(uid.toString(), Integer.valueOf(currentpage.toString()), Integer.valueOf(currentsize.toString()));
                if(userPageInfo.size() > 0){
                    return  MsgResult.add(ResultStatus.success).add("data",userPageInfo);
                }
            }
        }catch (Exception e){
            return MsgResult.add(ResultStatus.error).add("msg",e.getMessage());
        }
        return  MsgResult.add(ResultStatus.failure);
    }

}
