package org.myblog.blog.controller;


import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogFansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RequestMapping("/blog/blog-fans")
public class BlogFansController {

    @Autowired
    BlogFansService blogFansService;

    @PostMapping("/save")
    public MsgResult save(@RequestBody Map<String,Object> m, HttpServletRequest request, HttpServletResponse response){
        String requestCookieUid = new BlogHttpUtils().getRequestCookieUid(request);
        Object uid = m.get("uid");
        if(requestCookieUid != null && uid != null){
           Boolean isSave= blogFansService.save(requestCookieUid,uid.toString());
           if(isSave){
               return MsgResult.add(ResultStatus.success);
           }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/delFans")
    public MsgResult delFans(@RequestBody Map<String,Object> m, HttpServletRequest request, HttpServletResponse response){
        String requestCookieUid = new BlogHttpUtils().getRequestCookieUid(request);
        Object uid = m.get("uid");
        if(requestCookieUid != null && uid != null){
            Boolean isDel= blogFansService.delFans(requestCookieUid,uid.toString());
            if(isDel){
                return MsgResult.add(ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/isFans")
    public MsgResult isFans(@RequestBody Map<String,Object> m, HttpServletRequest request, HttpServletResponse response){
        String requestCookieUid = new BlogHttpUtils().getRequestCookieUid(request);
        Object uid = m.get("uid");
        if(requestCookieUid != null && uid != null){
            Boolean isF= blogFansService.isFans(requestCookieUid,uid.toString());
            if(isF){
                return MsgResult.add(ResultStatus.success);
            }else{
                return MsgResult.add(ResultStatus.no_fans);
            }
        }
        return MsgResult.add(ResultStatus.plz_login);
    }


}
