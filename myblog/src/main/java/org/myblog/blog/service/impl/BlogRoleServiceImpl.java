package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.*;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.mapper.BlogRoleMapper;
import org.myblog.blog.service.BlogAdminUserService;
import org.myblog.blog.service.BlogRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.service.BlogRsaService;
import org.myblog.blog.service.BlogUserService;

import org.myblog.blog.vo.admin.AdminRole;
import org.myblog.blog.vo.admin.AdminRoleUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @since 2021-07-17
 */
@Service
public class BlogRoleServiceImpl extends ServiceImpl<BlogRoleMapper, BlogRole> implements BlogRoleService {

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Override
    public String getRoleName(Integer rid)
    {
        LambdaQueryWrapper<BlogRole> lambdaQuery = Wrappers.<BlogRole>lambdaQuery();
        lambdaQuery.select(BlogRole::getName).eq(BlogRole::getId,rid);
        return this.getOne(lambdaQuery).getName();
    }

    @Override
    public List<BlogRole> getRoleList(String cookieUid) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogUser user = blogUserService.getUser(decrypt);
        if(user != null){
            BlogAdminUser adminUser = blogAdminUserService.getAdminUser(user.getId());
            if(adminUser.getRid() < AdminUserType.user.getId()){
                return this.list();
            }
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> listAdminRole(String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.admin.getId()) {
            return getAdminRole(valueOf,valueOf1);
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getAdminRole(IPage<BlogRole> page){
        List<HashMap<String, Object>> collect = page.getRecords().stream().map(blogRole -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            AdminRole adminRole = new AdminRole();
            BeanUtils.copyProperties(blogRole, adminRole);
            adminRole.setRoleName(this.getRoleName(adminRole.getChangeRole()));
            adminRole.setCroleName(this.getRoleName(adminRole.getCreateRole()));

            hashMap.put("role", adminRole);
            hashMap.put("total", page.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HashMap<String, Object>> getAdminRole(Integer valueOf, Integer valueOf1) {
        IPage<BlogRole> blogRoleIPage = new Page<>(valueOf, valueOf1);
        IPage<BlogRole> page = this.page(blogRoleIPage);
        return getAdminRole(page);
    }

    @Override
    public boolean adminRoleChange(AdminRole adminRole, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.admin.getId()){
            BlogRole blogRole = new BlogRole();
            BeanUtils.copyProperties(adminRole,blogRole);
            blogRole.setChangeRole(adminUser.getRid());
            blogRole.setChangeTime(nowTime);
            try {
                String roleName = this.getRoleName(adminRole.getId());
                return this.update(blogRole,new QueryWrapper<BlogRole>().eq("id",blogRole.getId()));
            }catch (Exception e){
                blogRole.setCreateTime(nowTime);
                blogRole.setCreateRole(adminUser.getRid());
                return this.save(blogRole);
            }
        }
        return false;
    }
    private void removeAdminByUID(String u) {
        this.remove(new QueryWrapper<BlogRole>().eq("id",u));
    }
    @Override
    public boolean adminRoleDelete(String rids, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() <2){
            String[] split = rids.split(",");
            if (split != null) {
                for (String u : split) {
                    removeAdminByUID(u);
                }
                return true;
            }else{
                try {
                    Integer value = Integer.valueOf(rids);
                    removeAdminByUID(rids);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultRole(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.admin.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminRole(cookieUid, valueOf, valueOf1);
            }
            Page<BlogRole> blogRolePage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogRole> wrapper = new QueryWrapper<>();
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
            Page<BlogRole> rolePage = this.page(blogRolePage, wrapper);
            return getAdminRole(rolePage);
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> listAdminRoleUser(String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.admin.getId()) {
            return getAdminRoleUser(valueOf,valueOf1);
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getAdminRoleUser(Page<BlogAdminUser> adminUserPage){
        List<HashMap<String, Object>> collect = adminUserPage.getRecords().stream().map(blogAdminUser -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            AdminRoleUser adminRoleUser = new AdminRoleUser();
            BeanUtils.copyProperties(blogAdminUser, adminRoleUser);
            adminRoleUser.setRoleName(this.getRoleName(adminRoleUser.getChangeRole()));
            adminRoleUser.setRname(this.getRoleName(adminRoleUser.getRid()));
            adminRoleUser.setUname(blogUserService.getUser(adminRoleUser.getUid().toString()).getName());
            hashMap.put("roleuser", adminRoleUser);
            hashMap.put("total", adminUserPage.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HashMap<String, Object>> getAdminRoleUser(Integer valueOf, Integer valueOf1) {
        Page<BlogAdminUser> blogAdminUserPage = new Page<>(valueOf, valueOf1);
        Page<BlogAdminUser> adminUserPage = blogAdminUserService.page(blogAdminUserPage);
        return getAdminRoleUser(adminUserPage);
    }

    @Override
    public boolean adminRoleUserChange(AdminRoleUser adminRoleUser, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.admin.getId()){
            BlogAdminUser blogAdminUser = new BlogAdminUser();
            BeanUtils.copyProperties(adminRoleUser,blogAdminUser);
            blogAdminUser.setChangeRole(adminUser.getRid());
            blogAdminUser.setChangeTime(nowTime);
            try {
                String roleName = this.getRoleName(adminRoleUser.getRid());
                return blogAdminUserService.update(blogAdminUser,new QueryWrapper<BlogAdminUser>().eq("uid",blogAdminUser.getUid()));
            }catch (Exception e){
                blogAdminUser.setCreateTime(nowTime);
                blogAdminUser.setCreateRole(adminUser.getRid());
                return blogAdminUserService.save(blogAdminUser);
            }
        }
        return false;
    }

    @Override
    public boolean adminRoleUserDelete(String ruids, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() <2){
            String[] split = ruids.split(",");
            if (split != null) {
                for (String u : split) {
                    blogAdminUserService.removeAdminByUID(u);
                }
                return true;
            }else{
                try {
                    Integer value = Integer.valueOf(ruids);
                    blogAdminUserService.removeAdminByUID(ruids);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultRoleUser(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.admin.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminRoleUser(cookieUid, valueOf, valueOf1);
            }
            Page<BlogAdminUser> blogAdminUserPage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogAdminUser> wrapper = new QueryWrapper<>();
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
            Page<BlogAdminUser> adminUserPage = blogAdminUserService.page(blogAdminUserPage, wrapper);
            return getAdminRoleUser(adminUserPage);
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getRoleAdminList(String cookieuid) {
        String decrypt = blogRsaService.decrypt(cookieuid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.admin.getId()) {
            return this.getBaseMapper().getRoleList();
        }
        return null;
    }
}
