package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.*;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.mapper.BlogFileMapper;
import org.myblog.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.myblog.blog.vo.admin.AdminFile;
import org.myblog.blog.vo.list.FileList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
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
public class BlogFileServiceImpl extends ServiceImpl<BlogFileMapper, BlogFile> implements BlogFileService {

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogCommitService blogCommitService;

    @Autowired
    BlogRoleService blogRoleService;

    @Override
    public Boolean save(String name, String dirPath, String localFilePath, String uid, String text, String tags, MultipartFile file,LocalDateTime time) {

        File file1 = new File(dirPath);
        File file2 = new File(localFilePath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        if(file2.exists()){
            file2.delete();
        }
        try {
            InputStream inputStream = file.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file2);
            byte[] buff = new byte[1024];
            int len=-1;
            while((len = inputStream.read(buff)) > -1){
                outputStream.write(buff,0,len);
            }
            outputStream.close();
            inputStream.close();
            ArrayList<Integer> list = new ArrayList<>();
            blogTagService.saveTags(tags.split(","),Integer.valueOf(uid));
            for (String TagName : tags.split(",")) {
                BlogTag blogTag = blogTagService.getOne(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getName, TagName).and(
                        a->{
                            a.eq(BlogTag::getUid,uid);
                        }
                ));
                if(blogTag != null){
                    list.add(blogTag.getId());
                }
            }
            BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(uid));
            BlogFile blogFile = new BlogFile();
            if(adminUser !=null){
                blogFile.setChangeRole(adminUser.getRid());
            }else{
                blogFile.setChangeRole(1);
            }
            blogFile.setChangeTime(time);
            blogFile.setCreateTime(time);
            blogFile.setDlink(localFilePath);
            blogFile.setName(name);
            blogFile.setSize(file.getSize());
            blogFile.setUid(Integer.valueOf(uid));
            blogFile.setTags(Arrays.toString(list.toArray()).replaceAll("\\s+","").replaceAll("\\[|\\]",""));
            blogFile.setText(text);
            return this.save(blogFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Page<FileList> getFileByUID(Page<?> page, Integer uid) {
        return this.getBaseMapper().getFileByUID(page,uid);
    }

    @Override
    public Page<FileList> getFileList(Page<?> page) {
        return this.getBaseMapper().getFileList(page);
    }

    @Override
    public BlogFile getFile(Integer id) {
        return this.getOne(new QueryWrapper<BlogFile>().eq("id",id));
    }

    @Override
    public Long getFilesByUID(Integer uid) {
        Page<BlogFile> blogFilePage = new Page<>(1,5);
        return this.getFileByUID(blogFilePage,uid).getTotal();
    }

    @Override
    public String saveEditImg(String name, String dirPath, String localFilePath, String uid,String auid, MultipartFile file) {
        LocalDateTime time = LocalDateTime.now().withNano(0);
        Boolean articleImg = this.save(name, dirPath, localFilePath, auid, "articleImg", "img,gif", file, time);
        if (articleImg) {
            BlogFile blogFile = this.getOne(new QueryWrapper<BlogFile>().eq("create_time", time).eq("change_time", time));
            if(blogFile != null){
                blogFile.setUid(Integer.valueOf(uid));
                this.update(blogFile,new QueryWrapper<BlogFile>().eq("id",blogFile.getId()));
                String url="/blog/blog-file/download?id="+blogFile.getId();
                return url;
            }
        }
        return null;
    }

    public List<HashMap<String, Object>> getAdminFiles(Page<BlogFile> filePage){
        List<HashMap<String, Object>> collect = filePage.getRecords().stream().map(blogFile -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            AdminFile adminFile = new AdminFile();
            BeanUtils.copyProperties(blogFile, adminFile);
            BlogUser user = blogUserService.getUser(adminFile.getUid().toString());
            adminFile.setUname(user.getName());
            adminFile.setCommits(blogCommitService.getFileCommits(adminFile.getId().toString()));
            adminFile.setRoleName(blogRoleService.getRoleName(adminFile.getChangeRole()));
            List<String> blogTags = blogTagService.getTags(adminFile.getTags());
            String[] ts = new String[blogTags.size()];
            for (int i = 0; i < blogTags.size(); i++) {
                ts[i] = blogTags.get(i);
            }
            adminFile.setTagsName(ts);
            hashMap.put("file", adminFile);
            hashMap.put("total", filePage.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<HashMap<String, Object>> getAdminFiles(Integer currpage, Integer currsize) {
        Page<BlogFile> blogFilePage = new Page<>(currpage, currsize);
        Page<BlogFile> filePage = this.page(blogFilePage);
        return getAdminFiles(filePage);
    }

    @Override
    public List<HashMap<String, Object>> listAdminFile(String cookieUid, Integer valueOf, Integer valueOf1) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            return getAdminFiles(valueOf,valueOf1);
        }else{
            Page<BlogFile> blogFilePage = new Page<>(valueOf,valueOf1);
            QueryWrapper<BlogFile> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(BlogFile::getUid,cookieUid);
            Page<BlogFile> filePage = this.page(blogFilePage,wrapper);
            return getAdminFiles(filePage);
        }
    }
    public String saveTags(Object arr[]){
        return Arrays.toString(arr).replaceAll("\\s+","").replaceAll("\\[|\\]","");
    }

    @Override
    public boolean adminFileChange(AdminFile adminFile, String cookieUid, LocalDateTime nowTime) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if(adminUser != null && adminUser.getRid() <= AdminUserType.user.getId()){
            blogTagService.saveTags(adminFile.getTagsName(),adminFile.getUid());
            BlogFile blogFile = new BlogFile();
            String[] articleTagsName = adminFile.getTagsName();
            String[] tags=new String[articleTagsName.length];
            BeanUtils.copyProperties(adminFile,blogFile);
            for (int i = 0; i < articleTagsName.length; i++) {
                BlogTag one = blogTagService.getOne(new QueryWrapper<BlogTag>().eq("uid", adminFile.getUid()).eq("name", articleTagsName[i]));
                tags[i]=one.getId().toString();
            }
            blogFile.setTags(saveTags(tags));
            blogFile.setChangeRole(adminUser.getRid());
            blogFile.setChangeTime(nowTime);
            String dlink1 = blogFile.getDlink();
            File file1 = new File(dlink1);
            if(file1.exists()){
                blogFile.setSize(file1.length());
            }else{
                blogFile.setSize(0L);
            }
            BlogFile blogFile2 = this.getOne(new QueryWrapper<BlogFile>().eq("id", adminFile.getId()));
            if(blogFile2 != null){
                String dlink = blogFile2.getDlink();
                File file = new File(dlink);
                if(file.exists()){
                    file.delete();
                }
            }
            if(blogFile.getId() != null){
                return this.update(blogFile,new QueryWrapper<BlogFile>().eq("id",blogFile.getId()).eq("uid",blogFile.getUid()));
            }else{
                blogFile.setCreateTime(nowTime);
                return this.save(blogFile);
            }
        }
        return false;
    }

    private void removeAdminByUID(String u) {
        QueryWrapper<BlogFile> wrapper = new QueryWrapper<BlogFile>().eq("id", u);
        BlogFile one = this.getOne(wrapper);
        File file = new File(one.getDlink());
        if(file.exists()){
            file.delete();
            blogCommitService.remove(new QueryWrapper<BlogCommit>().eq("id",u).eq("type",1));
            this.remove(wrapper);
        }
    }

    @Override
    public boolean adminFileDelete(String fids, String cookieUid, LocalDateTime nowTime) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if(adminUser != null && adminUser.getRid() <= AdminUserType.user.getId()){
            String[] split = fids.split(",");
            if (split != null) {
                for (String u : split) {
                    removeAdminByUID(u);
                }
                return true;
            }else{
                try {
                    Integer value = Integer.valueOf(fids);
                    removeAdminByUID(fids);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }

        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultAdminFile(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(cookieUid));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminFile(cookieUid, valueOf, valueOf1);
            }
            Page<BlogFile> blogFilePage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogFile> wrapper = new QueryWrapper<>();
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
            if(adminUser.getRid() == AdminUserType.user.getId()){
                wrapper.and(a->{
                    a.lambda().eq(BlogFile::getUid,cookieUid);
                });
            }
            Page<BlogFile> filePage = this.page(blogFilePage, wrapper);
            return getAdminFiles(filePage);
        }
        return null;
    }

    @Override
    public Boolean uploadFile(String name, String dirPath, String localFilePath, String uid, MultipartFile file, LocalDateTime time) {
        File file1 = new File(dirPath);
        File file2 = new File(localFilePath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        if(file2.exists()){
            file2.delete();
        }
        try {
            InputStream inputStream = file.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file2);
            byte[] buff = new byte[1024];
            int len=-1;
            while((len = inputStream.read(buff)) > -1){
                outputStream.write(buff,0,len);
            }
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getTitleList() {
        return this.getBaseMapper().getTitleList();
    }


}
