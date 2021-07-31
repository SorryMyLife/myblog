package org.myblog.blog.extend.extract;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.myblog.blog.entity.BlogAdminTableColumns;
import org.myblog.blog.entity.BlogAdminUser;
import org.myblog.blog.entity.BlogCode;
import org.myblog.blog.entity.BlogVerif;
import org.myblog.blog.vo.admin.*;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExtractExcel {

    public File getExcelFile() {
        return excelFile;
    }

    private File excelFile;


    public InputStream blodAdminUserExcel(String fileName, BlogAdminUser adminUser,List<BlogAdminTableColumns> blogAdminTableColumns,List<HashMap<String, Object>> adminUsers){
        try {
            File file = checkFile(adminUser, fileName);
            writeByUserExcel(file,blogAdminTableColumns,adminUsers);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public InputStream blodAdminArticleExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminArticles) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByArticleExcel(file,blogAdminTableColumns,adminArticles);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminFileExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminFiles) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByFileExcel(file,blogAdminTableColumns,adminFiles);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminRoleExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminRole) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByRoleExcel(file,blogAdminTableColumns,adminRole);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminCommitExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminCommit) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByCommitExcel(file,blogAdminTableColumns,adminCommit);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminRoleUserExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminRoleUser) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByRoleUserExcel(file,blogAdminTableColumns,adminRoleUser);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminLoginsExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminLogins) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByLoginsExcel(file,blogAdminTableColumns,adminLogins);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminCodesExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminCodes) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByCodesExcel(file,blogAdminTableColumns,adminCodes);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream blodAdminVerifsExcel(String fileName, BlogAdminUser adminUser, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminVerifs) {
        try {
            File file = checkFile(adminUser, fileName);
            writeByVerifsExcel(file,blogAdminTableColumns,adminVerifs);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File checkFile(BlogAdminUser adminUser,String fileName){
        String dirPath="uid/"+adminUser.getUid()+"/excel";
        File file1 = new File(dirPath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        String out_path=dirPath+"/"+fileName;
        File file = new File(out_path);
        if(file.exists()){
            file.delete();
        }
        this.excelFile=file;
        return file;
    }

    private CellStyle style(HSSFWorkbook excel){
        CellStyle style = excel.createCellStyle();
        style.setFont(font(excel));
        style.setAlignment(HorizontalAlignment.CENTER);        //横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//纵向居中
        return style;
    }

    private Font font(HSSFWorkbook excel){
        Font font = excel.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        return font;
    }

    private HSSFSheet sheet(HSSFWorkbook excel,String name){
        HSSFSheet sheet = excel.createSheet(name);
        sheet.setDefaultColumnWidth(24);
        return sheet;
    }


    private void writeByUserExcel(File file,List<BlogAdminTableColumns> blogAdminTableColumns,List<HashMap<String, Object>> adminUsers) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"用户信息");
        CellStyle style = style(excel);
        int size = adminUsers.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminUsers.get(i-1);
            AdminUser user_info = (AdminUser) hashMap.get("user_info");
            setAdminUserCellValue(style,row,hashMap,user_info);
        }
        excel.write(file);
    }

    private void writeByArticleExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminArticles) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"文章");
        CellStyle style = style(excel);
        int size = adminArticles.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminArticles.get(i-1);
            AdminArticle article = (AdminArticle) hashMap.get("article");
            setAdminArticleCellValue(style,row,hashMap,article);
        }
        excel.write(file);
    }


    private void writeByFileExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminFiles) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"文件");
        CellStyle style = style(excel);
        int size = adminFiles.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminFiles.get(i-1);
            AdminFile adminFileMap = (AdminFile) hashMap.get("file");
            setAdminFileCellValue(style,row,hashMap,adminFileMap);
        }
        excel.write(file);
    }

    private void writeByRoleExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminRole) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"角色");
        CellStyle style = style(excel);
        int size = adminRole.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminRole.get(i-1);
            AdminRole role = (AdminRole) hashMap.get("role");
            setAdminRoleCellValue(style,row,hashMap,role);
        }
        excel.write(file);
    }

    private void writeByCommitExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminCommit) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"评论");
        CellStyle style = style(excel);
        int size = adminCommit.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminCommit.get(i-1);
            AdminCommit commit = (AdminCommit) hashMap.get("commit");
            setAdminCommitCellValue(style,row,hashMap,commit);
        }
        excel.write(file);
    }

    private void writeByRoleUserExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminRoleUser) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"用户角色");
        CellStyle style = style(excel);
        int size = adminRoleUser.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminRoleUser.get(i-1);
            AdminRoleUser roleuser = (AdminRoleUser) hashMap.get("roleuser");
            setAdminRoleUserCellValue(style,row,hashMap,roleuser);
        }
        excel.write(file);
    }

    private void writeByLoginsExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminLogins) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"登录信息");
        CellStyle style = style(excel);
        int size = adminLogins.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminLogins.get(i-1);
            AdminLoginList llist = (AdminLoginList) hashMap.get("llist");
            setAdminLoginsCellValue(style,row,hashMap,llist);
        }
        excel.write(file);
    }

    private void writeByCodesExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminCodes) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"文本验证");
        CellStyle style = style(excel);
        int size = adminCodes.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminCodes.get(i-1);
            BlogCode clist = (BlogCode) hashMap.get("clist");
            setAdminCodesCellValue(style,row,hashMap,clist);
        }
        excel.write(file);
    }

    private void writeByVerifsExcel(File file, List<BlogAdminTableColumns> blogAdminTableColumns, List<HashMap<String, Object>> adminVerifs) throws IOException {
        HSSFWorkbook excel = new HSSFWorkbook();
        HSSFSheet sheet = sheet(excel,"登录信息");
        CellStyle style = style(excel);
        int size = adminVerifs.size();
        HSSFRow row = sheet.createRow(0);
        writeLine0(style,row,blogAdminTableColumns);
        for (int i = 1; i <=size ; i++) {
            row = sheet.createRow(i);
            HashMap<String, Object> hashMap = adminVerifs.get(i-1);
            BlogVerif vlist = (BlogVerif) hashMap.get("vlist");
            setAdminVserifsCellValue(style,row,hashMap,vlist);
        }
        excel.write(file);
    }


    private void setAdminLoginsCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, AdminLoginList llist) {
        setCellValue(style,row,llist.getUid(),llist.getUname(),llist.getLoginErrorCount(),
                llist.getLoginCount(),llist.getIpAddr(),llist.getCreateTime(),llist.getChangeTime(),
                llist.getRoleName(),llist.getCroleName());
    }
    private void setAdminCodesCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, BlogCode clist) {
        setCellValue(style,row,clist.getUid(),clist.getCid(),clist.getCode());
    }

    private void setAdminVserifsCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, BlogVerif vlist) {
        setCellValue(style,row,vlist.getUid(),vlist.getCid(),vlist.getSliderX());
    }

    private void setAdminRoleUserCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, AdminRoleUser roleuser) {
        setCellValue(style,row,roleuser.getUid(),roleuser.getUname(),roleuser.getRid()
                ,roleuser.getRname(),roleuser.getCreateTime(),roleuser.getChangeTime(),roleuser.getRoleName());
    }

    private void setAdminCommitCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, AdminCommit commit) {
        setCellValue(style,row,commit.getTitle(),commit.getId(),commit.getUid(),commit.getUname()
                ,commit.getHtml(),ArrayToString(commit.getTagsName()),commit.getTypeName()
                ,commit.getCreateTime(),commit.getChagneTime(),commit.getRoleName());
    }


    private void setAdminRoleCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, AdminRole role) {
        setCellValue(style,row,role.getId(),role.getName(),role.getCroleName()
                ,role.getRoleName(),role.getCreateTime(),role.getChangeTime());
    }

    private void setAdminUserCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, AdminUser user_info){
        setCellValue(style,row,user_info.getId(),user_info.getName(),user_info.getAutograph()
                ,user_info.getGender()==0?"男":"女",user_info.getAddress2(),user_info.getPhone(),user_info.getEmail()
                ,user_info.getCreateTime(),user_info.getCroleName(),user_info.getChangeTime(),user_info.getRoleName()
                ,user_info.getLoginType(),getByMapValue(hashMap,"role_name"),getByMapValue(hashMap,"articles")
                ,getByMapValue(hashMap,"files"),getByMapValue(hashMap,"tags"),getByMapValue(hashMap,"fans"),getByMapValue(hashMap,"commits"));
    }

    private void setAdminArticleCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, AdminArticle article){
        setCellValue(style,row,article.getTitle(),article.getId(),article.getUid(),article.getUname()
                ,article.getHtml(),ArrayToString(article.getTagsName()),article.getCommits(),article.getCreateTime()
                ,article.getChangeTime(),article.getRoleName());
    }
    private void setAdminFileCellValue(CellStyle style, HSSFRow row, HashMap<String, Object> hashMap, AdminFile adminFileMap) {
        setCellValue(style,row,adminFileMap.getName(),adminFileMap.getId(),
                adminFileMap.getUid(),adminFileMap.getUname(),adminFileMap.getHtml(),
                adminFileMap.getDlink(),ArrayToString(adminFileMap.getTagsName()),adminFileMap.getCommits()
                ,adminFileMap.getSize(),adminFileMap.getCreateTime(),adminFileMap.getChangeTime(),adminFileMap.getRoleName());
    }

    private void setCellValue(CellStyle style, HSSFRow row,Object... value){
        for (int i = 0; i < value.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(value[i]==null ? "" :value[i].toString());
            cell.setCellStyle(style);
        }
    }

    private String getByMapValue(Map<String,Object> map, String key){
        return map.get(key).toString();
    }
    private String ArrayToString(String a[]){
        return Arrays.toString(a).replaceAll("\\[|\\]","").replaceAll("\\s+","");
    }

    private void writeLine0(CellStyle style,HSSFRow row,List<BlogAdminTableColumns> blogAdminTableColumns){
        int columnsSize = blogAdminTableColumns.size();
        for (int i1 = 0; i1 < columnsSize; i1++) {
            HSSFCell cell = row.createCell(i1);
            cell.setCellValue(blogAdminTableColumns.get(i1).getLabel());
            cell.setCellStyle(style);
        }
    }


}
