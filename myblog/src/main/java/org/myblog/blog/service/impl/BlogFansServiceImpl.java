package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.myblog.blog.entity.BlogFans;
import org.myblog.blog.entity.BlogUser;
import org.myblog.blog.mapper.BlogFansMapper;
import org.myblog.blog.service.BlogFansService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.service.BlogRsaService;
import org.myblog.blog.service.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Service
public class BlogFansServiceImpl extends ServiceImpl<BlogFansMapper, BlogFans> implements BlogFansService {

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogUserService blogUserService;

    @Override
    public Boolean save(String requestCookieUid, String uid) {
        String decrypt = blogRsaService.decrypt(requestCookieUid);
        BlogUser user = blogUserService.getUser(uid);
        if(user != null){
            if(uid.equals(decrypt)){
                return false;
            }
            LambdaQueryWrapper<BlogFans> blogFansLambdaQueryWrapper = Wrappers.<BlogFans>lambdaQuery();
            blogFansLambdaQueryWrapper.eq(BlogFans::getFansId,decrypt).and(
              a->{
                  a.eq(BlogFans::getUid,uid);
              }
            );
            BlogFans fans = this.getOne(blogFansLambdaQueryWrapper);
            if(fans ==null){
                LocalDateTime time = LocalDateTime.now().withNano(0);
                BlogFans blogFans = new BlogFans();
                blogFans.setChangeRole(1);
                blogFans.setChangeTime(time);
                blogFans.setCreateTime(time);
                blogFans.setFansId(Integer.valueOf(decrypt));
                blogFans.setUid(user.getId());
                return this.save(blogFans);
            }
        }
        return false;
    }

    @Override
    public Boolean delFans(String requestCookieUid, String uid) {
        String decrypt = blogRsaService.decrypt(requestCookieUid);
        BlogUser user = blogUserService.getUser(uid);
        if(user != null){
            LambdaQueryWrapper<BlogFans> blogFansLambdaQueryWrapper = Wrappers.<BlogFans>lambdaQuery();
            blogFansLambdaQueryWrapper.eq(BlogFans::getFansId,decrypt).and(a->{
                a.eq(BlogFans::getUid,user.getId());
            });
            return this.remove(blogFansLambdaQueryWrapper);
        }
        return false;
    }

    @Override
    public Boolean isFans(String requestCookieUid, String uid) {
        String decrypt = blogRsaService.decrypt(requestCookieUid);
        BlogUser user = blogUserService.getUser(uid);
        if(user != null){
            LambdaQueryWrapper<BlogFans> blogFansLambdaQueryWrapper = Wrappers.<BlogFans>lambdaQuery();
            blogFansLambdaQueryWrapper.eq(BlogFans::getFansId,decrypt).and(a->{
                a.eq(BlogFans::getUid,user.getId());
            });
            return this.getOne(blogFansLambdaQueryWrapper)!=null;
        }
        return false;
    }

    @Override
    public Integer getFansByUID(Integer uid)
    {
        LambdaQueryWrapper<BlogFans> query = Wrappers.<BlogFans>lambdaQuery();
        query.eq(BlogFans::getUid,uid);
        return this.count(query);
    }
}
