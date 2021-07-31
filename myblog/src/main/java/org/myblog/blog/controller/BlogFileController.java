package org.myblog.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogCommit;
import org.myblog.blog.entity.BlogFile;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogCommitService;
import org.myblog.blog.service.BlogFileService;
import org.myblog.blog.service.BlogRsaService;
import org.myblog.blog.service.BlogTagService;

import org.myblog.blog.vo.admin.AdminFile;
import org.myblog.blog.vo.list.FileList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/blog/blog-file")
public class BlogFileController {

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogFileService blogFileService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogCommitService blogCommitService;

    @PostMapping("/saveEditImg")
    public MsgResult saveEditImg(@RequestParam("uid") String uid, @RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        if(uid == null || uid.isEmpty()){
            String cookieUid = blogHttpUtils.getRequestCookieUid(request);
            uid = blogRsaService.decrypt(cookieUid);
        }
        if(file != null && uid != null){
            String auid = blogHttpUtils.getRequestCookieUid(request);
            if(auid != null){
                auid = blogRsaService.decrypt(auid);
                String suffix = file.getOriginalFilename();
                suffix=suffix.substring(suffix.lastIndexOf("."));
                String name = blogHttpUtils.getDATE()+suffix;
                String dirPath="uid/"+uid;
                String localFilePath = dirPath+"/"+name;
                String result = blogFileService.saveEditImg(name, dirPath, localFilePath,uid, auid, file);
                if(result != null){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success).add("data",result);
                }
            }
            return MsgResult.add(ResultStatus.upload_file_error);
        }
        return MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/save")
    public MsgResult save(@RequestParam("tags") String tags,@RequestParam("text") String text,@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){

        if(file != null && text != null && tags != null){
            BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
            String uid = blogHttpUtils.getRequestCookieUid(request);
            if(uid != null){
                uid = blogRsaService.decrypt(uid);
                String suffix = file.getOriginalFilename();
                suffix=suffix.substring(suffix.lastIndexOf("."));
                String name = blogHttpUtils.getDATE()+suffix;
                String dirPath="uid/"+uid;
                String localFilePath = dirPath+"/"+name;
                LocalDateTime time = LocalDateTime.now().withNano(0);
                Boolean save = blogFileService.save(name, dirPath, localFilePath, uid, text, tags, file,time);
                if(save){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success);
                }
            }
            return MsgResult.add(ResultStatus.upload_file_error);
        }
        return MsgResult.add(ResultStatus.failure);
    }

    @GetMapping("/download")
    public MsgResult download(@PathParam("id") Integer id, HttpServletRequest request, HttpServletResponse response){
        if(id != null){
            BlogFile blogFile = blogFileService.getFile(id);
            if(blogFile != null){
                File file = new File(blogFile.getDlink());
                if(file.exists()){
                    response.setCharacterEncoding("UTF-8");
                    OutputStream out = null;
                    BufferedInputStream is = null;
                    byte[] b = new  byte[4096];
                    int len = 1024;
                    // 清空response
                    response.reset();
                    try {
                        response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(file.getName(), "UTF-8"));//设置头部信息
                        out = response.getOutputStream();
                        is = new BufferedInputStream(new FileInputStream(file));
                        while((len= is.read(b)) != -1) {
                            out.write(b,0,len);
                        }
                    } catch (Exception e) {
                        return MsgResult.add(ResultStatus.error).add("msg",e.getMessage());
                    }finally {
                        try {
                            out.flush();
                            if(is!=null)
                                is.close();
                            if(out!=null)
                                out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return MsgResult.add(ResultStatus.file_download_error);
            }
        }
        return MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/getFileListByUID")
    public MsgResult getFileListByUID(@RequestBody Map<String,Object> map,HttpServletResponse response,HttpServletRequest request){
        Object uid = map.get("uid");
        Object currentpage = map.get("currentpage");
        Object currentsize = map.get("currentsize");
        if(currentpage != null && currentsize != null){
            Page<Map<String, Object>> mapPage = new Page<>(Integer.valueOf(currentpage.toString()),Integer.valueOf(currentsize.toString()));
            if(uid == null || uid.toString().isEmpty()){
                uid = new BlogHttpUtils().getRequestCookieUid(request);
                if(uid == null){
                    Page<FileList> fileList = blogFileService.getFileList(mapPage);
                    List<FileList> fileList2 = fileList.getRecords().stream().map(fileList1 -> {
                        int commits = blogCommitService.count(new QueryWrapper<BlogCommit>().eq("id", fileList1.getFid()).and(a -> {
                            a.eq("type", 1);
                        }));
                        fileList1.setCommits(commits);
                        return fileList1;
                    }).collect(Collectors.toList());
                    if(fileList2 != null){
                        if(fileList2.size() ==0){
                            return MsgResult.add(ResultStatus.article_commits);
                        }
                        return MsgResult.add(ResultStatus.success).add("data",fileList2).add("total",fileList.getTotal());
                    }
                }
                uid=blogRsaService.decrypt(uid.toString());
            }
            Page<FileList> fileByUID = blogFileService.getFileByUID(mapPage, Integer.valueOf(uid.toString()));
            List<FileList> fileList2 = fileByUID.getRecords().stream().map(fileList1 -> {
                int commits = blogCommitService.count(new QueryWrapper<BlogCommit>().eq("id", fileList1.getFid()).and(a -> {
                    a.eq("type", 1);
                }));
                fileList1.setCommits(commits);
                return fileList1;
            }).collect(Collectors.toList());
            if(fileByUID != null){
                if(fileList2.size() ==0){
                    return MsgResult.add(ResultStatus.article_commits);
                }
                return MsgResult.add(ResultStatus.success).add("data",fileList2).add("total",fileByUID.getTotal());
            }
        }
        return MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/getFileList")
    public MsgResult getFileList(@RequestBody Map<String,Object> map,HttpServletResponse response,HttpServletRequest request){
        Object currentpage = map.get("currentpage");
        Object currentsize = map.get("currentsize");
        if(currentpage != null && currentsize != null){
            Page<FileList> mapPage = new Page<>(Integer.valueOf(currentpage.toString()),Integer.valueOf(currentsize.toString()));
            Page<FileList> fileList = blogFileService.getFileList(mapPage);
            List<FileList> fileList2 = fileList.getRecords().stream().map(fileList1 -> {
                int commits = blogCommitService.count(new QueryWrapper<BlogCommit>().eq("id", fileList1.getFid()).and(a -> {
                    a.eq("type", 1);
                }));
                fileList1.setCommits(commits);
                return fileList1;
            }).collect(Collectors.toList());
            if(fileList != null){
                return MsgResult.add(ResultStatus.success).add("data",fileList2).add("total",fileList.getTotal());
            }
        }
        return MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/admin/list")
    public MsgResult adminFileList(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(currpage != null &&cookieUid!=null && pagesize != null){
            String decrypt = blogRsaService.decrypt(cookieUid);
            List<HashMap<String, Object>> files =blogFileService.listAdminFile(decrypt,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(files != null ){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",files);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/change")
    public MsgResult adminFileChange(@RequestBody AdminFile adminFile, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(adminFile != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            String decrypt = blogRsaService.decrypt(cookieUid);
            boolean save = blogFileService.adminFileChange(adminFile,decrypt,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/del")
    public MsgResult adminFileDelete(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        Object fids = map.get("fids");
        if(fids != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            String decrypt = blogRsaService.decrypt(cookieUid);
            boolean save = blogFileService.adminFileDelete(fids.toString(),decrypt,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/search")
    public MsgResult adminFileSearch(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String s_str = (String) map.get("s_str");
        String s_types = (String) map.get("s_types");
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(s_str != null && s_types != null && currpage != null &&cookieUid!=null && pagesize != null){
            String decrypt = blogRsaService.decrypt(cookieUid);
            List<HashMap<String, Object>> articles =blogFileService.searchMultAdminFile(s_str,s_types, decrypt,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/admin/uploadfile")
    public MsgResult adminFileUploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        if(file != null ){
            BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
            String uid = blogHttpUtils.getRequestCookieUid(request);
            if(uid != null){
                uid = blogRsaService.decrypt(uid);
                String suffix = file.getOriginalFilename();
                suffix=suffix.substring(suffix.lastIndexOf("."));
                String name = blogHttpUtils.getDATE()+suffix;
                String dirPath="uid/"+uid;
                String localFilePath = dirPath+"/"+name;
                LocalDateTime time = LocalDateTime.now().withNano(0);
                Boolean save = blogFileService.uploadFile(name, dirPath, localFilePath, uid,file,time);
                if(save){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success).add("data",localFilePath);
                }
            }
            return MsgResult.add(ResultStatus.upload_file_error);
        }
        return MsgResult.add(ResultStatus.failure);
    }


}
