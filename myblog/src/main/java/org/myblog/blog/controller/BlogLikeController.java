package org.myblog.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.myblog.blog.entity.BlogLike;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogLikeService;
import org.myblog.blog.service.BlogRsaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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
@RequestMapping("/blog/blog-like")
public class BlogLikeController {

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogLikeService blogLikeService;

    @PostMapping("/save")
    public MsgResult save(@RequestBody Map<String,Object> m, HttpServletRequest request, HttpServletResponse response){
        Object id = m.get("id");
        String requestCookieUid = new BlogHttpUtils().getRequestCookieUid(request);
        try {
            String decrypt = blogRsaService.decrypt(requestCookieUid);
            if(Integer.valueOf(decrypt) >0 && id != null){
                QueryWrapper<BlogLike> blogLikeQueryWrapper = new QueryWrapper<>();
                blogLikeQueryWrapper.eq("uid",decrypt).and(aa->{
                    aa.eq("like_id",id);
                });
                BlogLike one = blogLikeService.getOne(blogLikeQueryWrapper);
                if(one == null){
                    BlogLike blogLike = new BlogLike();
                    blogLike.setChangeRole(4);
                    blogLike.setChangeTime(LocalDateTime.now());
                    blogLike.setCreateTime(LocalDateTime.now());
                    blogLike.setLikeId(Integer.valueOf(id.toString()));
                    blogLike.setUid(Integer.valueOf(decrypt));
                    blogLikeService.save(blogLike);
                    return MsgResult.add(ResultStatus.success);
                }
                return MsgResult.add(ResultStatus.isLike);
            }
        }catch (Exception e){
            return  MsgResult.add(ResultStatus.error);
        }
        return MsgResult.add(ResultStatus.failure);
    }



}
