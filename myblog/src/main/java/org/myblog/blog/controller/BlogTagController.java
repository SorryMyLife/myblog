package org.myblog.blog.controller;


import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogRsaService;
import org.myblog.blog.service.BlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
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
@RequestMapping("/blog/blog-tag")
public class BlogTagController {


    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogRsaService blogRsaService;

    @GetMapping("/getTagNames")
    public MsgResult getTagNames(@PathParam("uid") Integer uid){

        List<Map<String, Object>> tagNames = blogTagService.getTagNames(uid);
        if(tagNames != null && tagNames.size() > 0){
            return  MsgResult.add(ResultStatus.success).add("data",tagNames);
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/intoTag")
    public MsgResult intoTag(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        Object uid = map.get("uid");
        Object tag_id = map.get("tag_id");
        Object currentpage = map.get("currentpage");
        Object currentsize = map.get("currentsize");
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        if(uid == null){
            uid=blogHttpUtils.getRequestCookieUid(request);
            if(uid != null){
                uid = blogRsaService.decrypt(uid.toString());
            }
        }
        if(uid != null && tag_id !=null && currentpage != null && currentsize != null){
          Map<String,Object> mappp=  blogTagService.getIntoTagPage(uid.toString(),Integer.valueOf(tag_id.toString()),Integer.valueOf(currentpage.toString()),Integer.valueOf(currentsize.toString()));
            if(mappp != null){
                if(blogHttpUtils.getRequestCookieUid(request) != null){
                    blogHttpUtils.RefreshCookie(request,response);
                }
                return MsgResult.add(ResultStatus.success).add("data",mappp);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }


}
