package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.*;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.mapper.BlogLoginMapper;
import org.myblog.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.vo.admin.AdminFile;
import org.myblog.blog.vo.admin.AdminLoginList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-30
 */
@Service
public class BlogLoginServiceImpl extends ServiceImpl<BlogLoginMapper, BlogLogin> implements BlogLoginService {

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogRoleService blogRoleService;

    @Override
    public BlogLogin getLoginUser(String uid) {
        QueryWrapper<BlogLogin> q = new QueryWrapper<>();
        q.lambda().eq(BlogLogin::getUid,uid);
        List<BlogLogin> list = this.list(q);
        int size = list.size();
        try {
            return list.get(size-1);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean saveOK(String uid, String ip) {
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        BlogLogin blogLogin = new BlogLogin();
        blogLogin.setIpAddr(ip);
        blogLogin.setIsDel(0);
        blogLogin.setUid(uid);
        blogLogin.setLoginErrorCount(0);
        blogLogin.setCreateTime(localDateTime);
        blogLogin.setCreateRole(1);
        blogLogin.setLoginCount(1);
        blogLogin.setChangeRole(1);
        blogLogin.setChangeTime(localDateTime);
        BlogLogin loginUser = this.getLoginUser(uid);
        if(loginUser != null){
            blogLogin.setChangeTime(localDateTime);
            blogLogin.setChangeRole(1);
            blogLogin.setLoginCount(loginUser.getLoginCount()+1);
        }
        return this.save(blogLogin);
    }

    @Override
    public boolean saveError(String uid, String ip) {
        LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
        BlogLogin blogLogin = new BlogLogin();
        blogLogin.setIpAddr(ip);
        blogLogin.setIsDel(0);
        blogLogin.setUid(uid);
        blogLogin.setChangeRole(1);
        blogLogin.setChangeTime(localDateTime);
        blogLogin.setCreateTime(localDateTime);
        blogLogin.setCreateRole(1);
        blogLogin.setLoginErrorCount(0);
        blogLogin.setLoginCount(1);
        BlogLogin loginUser = this.getLoginUser(uid);
        if(loginUser != null){
            blogLogin.setCreateTime(localDateTime);
            blogLogin.setCreateRole(loginUser.getCreateRole());
            blogLogin.setLoginErrorCount(loginUser.getLoginErrorCount()+1);
            blogLogin.setLoginCount(loginUser.getLoginCount()+1);
        }
        return this.save(blogLogin);
    }

    @Override
    public Integer getErrorCount(String uid) {
        BlogLogin loginUser = this.getLoginUser(uid);
        return loginUser != null? loginUser.getLoginErrorCount():0;
    }

    @Override
    public List<HashMap<String, Object>> listAdminLoginList(String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            return getAdminLogins(valueOf,valueOf1);
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getAdminLogins(Page<BlogLogin> page){
        List<HashMap<String, Object>> collect = page.getRecords().stream().map(blogLogin -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            AdminLoginList adminLoginList = new AdminLoginList();
            BeanUtils.copyProperties(blogLogin, adminLoginList);
            BlogUser user = blogUserService.getUser(blogLogin.getUid());
            if(user == null){
                user = new BlogUser();
                user.setName("未知用戶");
            }
            adminLoginList.setUname(user.getName());
            adminLoginList.setRoleName(blogRoleService.getRoleName(adminLoginList.getChangeRole()));
            adminLoginList.setCroleName(blogRoleService.getRoleName(adminLoginList.getCreateRole()));
            hashMap.put("llist", adminLoginList);
            hashMap.put("total", page.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HashMap<String, Object>> getAdminLogins(Integer valueOf, Integer valueOf1) {
        Page<BlogLogin> blogLoginPage = new Page<>(valueOf, valueOf1);
        Page<BlogLogin> page = this.page(blogLoginPage);
        return getAdminLogins(page);
    }

    @Override
    public boolean adminLoginChange(AdminLoginList adminLoginList, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            BlogLogin blogLogin = new BlogLogin();
            BeanUtils.copyProperties(adminLoginList,blogLogin);
            blogLogin.setChangeRole(adminUser.getRid());
            blogLogin.setChangeTime(nowTime);
            if(blogLogin.getUid() != null){
                return this.update(blogLogin,new QueryWrapper<BlogLogin>().eq("uid",blogLogin.getUid()).eq("create_time",blogLogin.getCreateTime()));
            }else{
                blogLogin.setCreateTime(nowTime);
                blogLogin.setCreateRole(adminUser.getRid());
                return this.save(blogLogin);
            }
        }
        return false;
    }

    private void removeAdminByUID(String u,LocalDateTime time) {
        QueryWrapper<BlogLogin> wrapper = new QueryWrapper<BlogLogin>();
        wrapper.lambda().eq(BlogLogin::getUid,u).eq(BlogLogin::getCreateTime,time);
        this.remove(wrapper);
    }

    @Override
    public boolean adminLoginDelete(String lids, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            String[] split = lids.split(",");
            if (split != null) {
                for (String u : split) {
                    String[] split1 = u.split("----");
                    removeAdminByUID(split1[0], LocalDateTime.parse(split1[1]));
                }
                return true;
            }else{
                try {
                    String[] split1 = lids.split("----");
                    removeAdminByUID(split1[0], LocalDateTime.parse(split1[1]));
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultAdminLogin(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminLoginList(cookieUid, valueOf, valueOf1);
            }
            Page<BlogLogin> blogLoginPage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogLogin> wrapper = new QueryWrapper<>();
            String[] split = s_types.split(",");
            if(split != null){
                for (String s : split) {
                    String byCloum = searchMultType.getByCloum(s);
                    if(byCloum!=null){
                        wrapper.or().like(byCloum,s_str);
                    }
                }
            }else{
                String byCloum = searchMultType.getByCloum(s_types);
                if(byCloum!=null){
                    wrapper.like(byCloum,s_str);
                }
            }
            Page<BlogLogin> page = this.page(blogLoginPage, wrapper);
            return getAdminLogins(page);
        }
        return null;
    }
}
