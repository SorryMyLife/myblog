package org.myblog.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.myblog.blog.entity.BlogAdminTableColumns;
import org.myblog.blog.entity.BlogAdminUser;
import org.myblog.blog.extend.*;
import org.myblog.blog.extend.extract.ExtractExcel;
import org.myblog.blog.extend.extract.ExtractPDF;
import org.myblog.blog.extend.extract.ExtractWord;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.ExcelExtractColumType;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import static org.myblog.blog.extend.type.ExcelExtractColumType.*;

@RestController
@RequestMapping("/blog/extract")
public class BlogExtractController {

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Autowired
    BlogAdminTableColumnsService columnsService;

    @Autowired
    BlogArticleService blogArticleService;

    @Autowired
    BlogFileService blogFileService;

    @Autowired
    BlogFansService blogFansService;

    @Autowired
    BlogLikeService blogLikeService;

    @Autowired
    BlogRoleService blogRoleService;

    @Autowired
    BlogTagService blogTagService;

    @Autowired
    BlogCommitService blogCommitService;

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogCodeService blogCodeService;

    @Autowired
    BlogVerifService blogVerifService;

    @Autowired
    BlogLoginService blogLoginService;

    private String fileName;

    private InputStream inputStream;

    private ExtractExcel extractExcel;

    private void ExtractExcel(Integer ccctype, Integer line, BlogAdminUser adminUser, BlogHttpUtils blogHttpUtils){
        List<BlogAdminTableColumns> blogAdminTableColumns=null;
        switch (ExcelExtractColumType.getByType(ccctype)){
            case user:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", user.getType()));
                List<HashMap<String, Object>> adminUsers = blogAdminUserService.getAdminUsers(1, line==0?blogAdminTableColumns.size():line);
                fileName="adminUser"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminUserExcel(fileName, adminUser,blogAdminTableColumns,adminUsers);
                break;
            case article:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", article.getType()));
                List<HashMap<String, Object>> adminArticles = blogArticleService.getAdminArticles(1, line==0?blogAdminTableColumns.size():line);
                fileName="adminArticle"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminArticleExcel(fileName, adminUser,blogAdminTableColumns,adminArticles);
                break;
            case file:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", file.getType()));
                List<HashMap<String, Object>> adminFiles = blogFileService.getAdminFiles(1, line==0?blogAdminTableColumns.size():line);
                fileName="adminFile"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminFileExcel(fileName, adminUser,blogAdminTableColumns,adminFiles);
                break;
            case role:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", role.getType()));
                List<HashMap<String, Object>> adminRole = blogRoleService.getAdminRole(1, line==0?blogAdminTableColumns.size():line);
                fileName="adminRole"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminRoleExcel(fileName, adminUser,blogAdminTableColumns,adminRole);
                break;
            case commit:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", commit.getType()));
                List<HashMap<String, Object>> adminCommit = blogCommitService.getAdminCommit(1, line==0?blogAdminTableColumns.size():line);
                fileName="adminCommit"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminCommitExcel(fileName, adminUser,blogAdminTableColumns,adminCommit);
                break;
            case roleuser:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", roleuser.getType()));
                List<HashMap<String, Object>> adminRoleUser = blogRoleService.getAdminRoleUser(1, line==0?blogAdminTableColumns.size():line);
                fileName="adminRoleUser"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminRoleUserExcel(fileName, adminUser,blogAdminTableColumns,adminRoleUser);
                break;
            case loginlist:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", loginlist.getType()));
                List<HashMap<String, Object>> adminLogins = blogLoginService.getAdminLogins(1, line == 0 ? blogAdminTableColumns.size() : line);
                fileName="adminLogins"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminLoginsExcel(fileName, adminUser,blogAdminTableColumns,adminLogins);
                break;
            case textcode:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", textcode.getType()));
                List<HashMap<String, Object>> adminCodes = blogCodeService.getAdminCodes(1, line == 0 ? blogAdminTableColumns.size() : line);
                fileName="adminCodes"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminCodesExcel(fileName, adminUser,blogAdminTableColumns,adminCodes);
                break;
            case slider:
                blogAdminTableColumns = columnsService.list(new QueryWrapper<BlogAdminTableColumns>().eq("ccctype", slider.getType()));
                List<HashMap<String, Object>> adminVerifs = blogVerifService.getAdminVerifs(1, line == 0 ? blogAdminTableColumns.size() : line);
                fileName="adminVerifs"+blogHttpUtils.getDATE()+".xls";
                inputStream=extractExcel.blodAdminVerifsExcel(fileName, adminUser,blogAdminTableColumns,adminVerifs);
                break;
        }
    }

    private void returnDownload(String fileName,InputStream inputStream, HttpServletResponse response){
        OutputStream out=null;
        response.reset();
        response.setCharacterEncoding("UTF-8");
        try {
            response.setHeader("Content-disposition","attachment;filename="+URLEncoder.encode(fileName,"utf-8"));//设置头部信息
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            byte[] b = new  byte[4096];
            int len = 1024;
            out = response.getOutputStream();
            while((len=inputStream.read(b)) != -1){
                out.write(b,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.flush();
                if(inputStream!=null)
                    inputStream.close();
                if(out!=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BlogAdminUser checkAdmin(BlogHttpUtils blogHttpUtils,HttpServletResponse response,HttpServletRequest request){
        String requestCookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(requestCookieUid != null) {
            String decrypt = blogRsaService.decrypt(requestCookieUid);
            BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
            if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
                return adminUser;
            }
        }
            return null;
    }

    @GetMapping("/ExtractExcel")
    public MsgResult ExtractExcel(@PathParam("ccctype") Integer ccctype, @PathParam("line") Integer line, HttpServletRequest request, HttpServletResponse response){
        BlogHttpUtils blogHttpUtils =new BlogHttpUtils();
        BlogAdminUser adminUser = checkAdmin(blogHttpUtils, response, request);
        if(adminUser != null && line > -1){
            try {
                extractExcel=new ExtractExcel();
                ExtractExcel(ccctype, line,adminUser, blogHttpUtils);
                returnDownload(fileName,inputStream,response);
                blogHttpUtils.RefreshCookie(request,response);
                extractExcel.getExcelFile().delete();
            }catch (Exception e){
                e.printStackTrace();
                return MsgResult.add(ResultStatus.admin_download_error);
            }
        }else{
            return MsgResult.add(ResultStatus.authorization_into_page);
        }
        return MsgResult.add(ResultStatus.plz_login);
    }

    @GetMapping("/ExtractPDF")
    public MsgResult ExtractPDF(@PathParam("ccctype") Integer ccctype, @PathParam("line") Integer line, HttpServletRequest request, HttpServletResponse response){
        BlogHttpUtils blogHttpUtils =new BlogHttpUtils();
        BlogAdminUser adminUser = checkAdmin(blogHttpUtils, response, request);
        if(adminUser != null && line > -1){
            try {
                extractExcel=new ExtractExcel();
                ExtractPDF extractPDF = new ExtractPDF();
                ExtractExcel(ccctype, line, adminUser, blogHttpUtils);
                extractPDF.setExtractExcel(extractExcel);
                InputStream stream = extractPDF.ExcelToPdf(this.inputStream);
                returnDownload(extractPDF.getOutPdfName(), stream,response);
                blogHttpUtils.RefreshCookie(request,response);
                extractExcel.getExcelFile().delete();
            }catch (Exception e){
                e.printStackTrace();
                return MsgResult.add(ResultStatus.admin_download_error);
            }
        }else{
            return MsgResult.add(ResultStatus.authorization_into_page);
        }
        return MsgResult.add(ResultStatus.plz_login);
    }

    @GetMapping("/ExtractWord")
    public MsgResult ExtractWord(@PathParam("ccctype") Integer ccctype, @PathParam("line") Integer line, HttpServletRequest request, HttpServletResponse response){
        BlogHttpUtils blogHttpUtils =new BlogHttpUtils();
        BlogAdminUser adminUser = checkAdmin(blogHttpUtils, response, request);
        if(adminUser != null && line > -1){
            try {
                extractExcel=new ExtractExcel();
                ExtractWord extractWord = new ExtractWord();
                ExtractExcel(ccctype, line, adminUser, blogHttpUtils);
                extractWord.setExtractExcel(extractExcel);
                InputStream stream = extractWord.ExcelToWord(inputStream);
                returnDownload(extractWord.getOutWordName(), stream,response);
                blogHttpUtils.RefreshCookie(request,response);
                extractExcel.getExcelFile().delete();
            }catch (Exception e){
                e.printStackTrace();
                return MsgResult.add(ResultStatus.admin_download_error);
            }
        }else{
            return MsgResult.add(ResultStatus.authorization_into_page);
        }
        return MsgResult.add(ResultStatus.plz_login);
    }

}
