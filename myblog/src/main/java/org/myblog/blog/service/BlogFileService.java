package org.myblog.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogFile;
import com.baomidou.mybatisplus.extension.service.IService;

import org.myblog.blog.vo.admin.AdminFile;
import org.myblog.blog.vo.list.FileList;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogFileService extends IService<BlogFile> {

    Boolean save(String name, String dirPath, String localFilePath, String uid, String text, String tags, MultipartFile file, LocalDateTime time);

    Page<FileList> getFileByUID(Page<?> page,Integer uid);

    Page<FileList> getFileList(Page<?> page);

    BlogFile getFile(Integer id);

    Long getFilesByUID(Integer uid);

    String saveEditImg(String name, String dirPath, String localFilePath, String uid,String auid, MultipartFile file);

    List<HashMap<String, Object>> getAdminFiles(Integer currpage,Integer currsize);

    List<HashMap<String, Object>> listAdminFile(String cookieUid, Integer valueOf, Integer valueOf1);

    boolean adminFileChange(AdminFile adminFile, String cookieUid, LocalDateTime nowTime);

    boolean adminFileDelete(String toString, String cookieUid, LocalDateTime nowTime);

    List<HashMap<String, Object>> searchMultAdminFile(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1);

    Boolean uploadFile(String name, String dirPath, String localFilePath, String uid, MultipartFile file, LocalDateTime time);

    List<Map<String, Object>> getTitleList();

}
