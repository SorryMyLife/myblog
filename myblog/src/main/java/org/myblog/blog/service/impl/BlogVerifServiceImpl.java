package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogAdminUser;
import org.myblog.blog.entity.BlogCode;
import org.myblog.blog.entity.BlogVerif;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.extend.utils.VerificationCode;
import org.myblog.blog.mapper.BlogVerifMapper;
import org.myblog.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
public class BlogVerifServiceImpl extends ServiceImpl<BlogVerifMapper, BlogVerif> implements BlogVerifService {

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogRoleService blogRoleService;

    @Override
    public BlogVerif getVerif(String uid, String cid) {
        QueryWrapper<BlogVerif> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BlogVerif::getUid,uid).eq(BlogVerif::getCid,cid);
        List<BlogVerif> list = this.list(wrapper);
        return list.get(list.size()-1);
    }

    @Override
    public HashMap<String, Object> getVerificationCode() {
        VerificationCode verificationCode = new VerificationCode();
        return verificationCode.getCode();
    }

    @Override
    public void truncate() {
        this.getBaseMapper().truncate();
    }

    @Override
    public boolean checkVerif(String uid, String cid, String x) {
        BlogVerif verif = getVerif(uid, cid);
        if(verif!=null){
            int xx = Integer.valueOf(x);
            Integer vx = Integer.valueOf(verif.getSliderX());
            Integer lx=vx-3;
            Integer rx=vx+3;
            if(xx >=lx && xx <= rx){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> listAdminVerifList(String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            return getAdminVerifs(valueOf,valueOf1);
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getAdminVerifs(Integer valueOf, Integer valueOf1) {
        Page<BlogVerif> blogVerifPage = new Page<>(valueOf, valueOf1);
        Page<BlogVerif> page = this.page(blogVerifPage);
        return getAdminVerifs(page);
    }

    @Override
    public List<HashMap<String, Object>> getAdminVerifs(Page<BlogVerif> page) {
        List<HashMap<String, Object>> collect = page.getRecords().stream().map(blogVerif -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("vlist", blogVerif);
            hashMap.put("total", page.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean adminVerifChange(BlogVerif blogVerif, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            if(blogVerif.getUid() != null){
                return this.update(blogVerif,new QueryWrapper<BlogVerif>().eq("uid",blogVerif.getUid()).eq("cid",blogVerif.getCid()));
            }else{
                return this.save(blogVerif);
            }
        }
        return false;
    }

    private void removeAdminByUID(String u,String cid) {
        QueryWrapper<BlogVerif> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BlogVerif::getUid,u).eq(BlogVerif::getCid,cid);
        this.remove(wrapper);
    }

    @Override
    public boolean adminVerifDelete(String vids, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            String[] split = vids.split(",");
            if (split != null) {
                for (String u : split) {
                    String[] split1 = u.split("----");
                    removeAdminByUID(split1[0], split1[1]);
                }
                return true;
            }else{
                try {
                    String[] split1 = vids.split("----");
                    removeAdminByUID(split1[0], split1[1]);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultAdminVerif(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminVerifList(cookieUid, valueOf, valueOf1);
            }
            Page<BlogVerif> blogVerifPage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogVerif> wrapper = new QueryWrapper<>();
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
            Page<BlogVerif> page = this.page(blogVerifPage, wrapper);
            return getAdminVerifs(page);
        }
        return null;
    }
}
