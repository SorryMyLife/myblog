package org.myblog.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.myblog.blog.entity.BlogCommit;
import org.myblog.blog.entity.BlogUser;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.extend.type.CommitType;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogArticleService;
import org.myblog.blog.service.BlogCommitService;
import org.myblog.blog.service.BlogRsaService;
import org.myblog.blog.service.BlogUserService;
import org.myblog.blog.vo.admin.AdminCommit;
import org.myblog.blog.vo.list.CommitList;
import org.myblog.blog.vo.submit.SubmitCommit;
import org.myblog.blog.vo.user.UserMessageForArticle;
import org.myblog.blog.vo.user.UserMessageForFans;
import org.myblog.blog.vo.user.UserMessageForFile;
import org.myblog.blog.vo.user.UserMessageForLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/blog/blog-commit")
public class BlogCommitController {

    @Autowired
    BlogArticleService blogArticleService;

    @Autowired
    BlogCommitService blogCommitService;

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogRsaService rsaService;

    BlogHttpUtils blogHttpUtils = new BlogHttpUtils();

    @PostMapping("/save")
    public MsgResult save(@RequestBody SubmitCommit submitCommit, HttpServletResponse response , HttpServletRequest request){

        String uid = blogHttpUtils.getRequestCookieUid(request);
        if(uid == null){
            return MsgResult.add(ResultStatus.plz_login);
        }
        uid = rsaService.decrypt(uid);
        if(uid != null){
            LocalDateTime localDateTime = LocalDateTime.now().withNano(0);
            BlogCommit blogCommit = new BlogCommit();
            blogCommit.setId(submitCommit.getId());
            blogCommit.setMsg(submitCommit.getMsg());
            blogCommit.setType(submitCommit.getType());
            blogCommit.setTags("0");
            blogCommit.setHtml(submitCommit.getHtml());
            blogCommit.setUid(Integer.valueOf(uid));
            blogCommit.setChagneTime(localDateTime);
            blogCommit.setCreateTime(localDateTime);
            blogCommit.setChangeRole(AdminUserType.user.getId());
            blogCommitService.save(blogCommit);
            return MsgResult.add(ResultStatus.success);
        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/list")
    public MsgResult listByID(@RequestBody CommitList commitList, HttpServletResponse response, HttpServletRequest request){
        IPage<BlogCommit> blogCommitPage = new Page<BlogCommit>(commitList.getCurrentpage(),commitList.getCurrentsize());

        IPage<BlogCommit> blogCommitIPage = blogCommitService.page(blogCommitPage, new QueryWrapper<BlogCommit>().eq("id", commitList.getId()).and(a -> {
                    a.eq("type", commitList.getType());
                }));
        List<HashMap<String, Object>> commits = blogCommitIPage.getRecords().stream().map(blogCommit -> {
            HashMap<String, Object> map1 = new HashMap<>();
            HashMap<String, Object> usermap = new HashMap<>();
            BlogUser blogUser = blogUserService.getUser(blogCommit.getUid().toString());
            map1.put("create_time", blogCommit.getCreateTime());
            map1.put("msg", blogCommit.getMsg());
            map1.put("html",blogCommit.getHtml());
            usermap.put("icon", blogUser.getIcon());
            usermap.put("id", blogUser.getId());
            usermap.put("name", blogUser.getName());
            map1.put("user_info", usermap);
            map1.put("total", blogCommitIPage.getTotal());
            return map1;
        }).collect(Collectors.toList());
        if(commits != null){
            if(commits.size()==0){
                return MsgResult.add(ResultStatus.article_commits);
            }
            BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
            if(blogHttpUtils.getRequestCookieUid(request) != null){
                blogHttpUtils.RefreshCookie(request,response);
            }
            return MsgResult.add(ResultStatus.success).add("data",commits);
        }
        return  MsgResult.add(ResultStatus.article_commit_get_error);
    }

    @PostMapping("/getUserMessage")
    public MsgResult getUserMessage(@RequestBody Map<String,Object> map,HttpServletResponse response,HttpServletRequest request){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object m_type = map.get("m_type");
        String uid = blogHttpUtils.getRequestCookieUid(request);
        if(uid == null){
            return MsgResult.add(ResultStatus.plz_login);
        }
        Object currentpage = map.get("currentpage");
        Object currentsize = map.get("currentsize");
        if(m_type != null && uid != null && currentpage != null&&currentsize != null){
            uid = rsaService.decrypt(uid);
            if(m_type.equals(CommitType.article.getCommitcode())){
                Page<UserMessageForArticle> messageOnArticle = blogCommitService.getMessageOnArticle(Integer.valueOf(currentpage.toString()), Integer.valueOf(currentsize.toString()), uid);
                if(messageOnArticle != null){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success).add("data",messageOnArticle.getRecords()).add("total",messageOnArticle.getTotal());
              }
            }
            if(m_type.equals(CommitType.file.getCommitcode())){
                Page<UserMessageForFile> messageOnFile = blogCommitService.getMessageOnFile(Integer.valueOf(currentpage.toString()), Integer.valueOf(currentsize.toString()), uid);
                if(messageOnFile != null){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success).add("data",messageOnFile.getRecords()).add("total",messageOnFile.getTotal());
                }
            }
            if(m_type.equals(CommitType.like.getCommitcode())){
                Page<UserMessageForLike> messageOnLike = blogCommitService.getMessageOnLike(Integer.valueOf(currentpage.toString()), Integer.valueOf(currentsize.toString()), uid);
                if(messageOnLike != null){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success).add("data",messageOnLike.getRecords()).add("total",messageOnLike.getTotal());
                }
            }

            if(m_type.equals(CommitType.fans.getCommitcode())){
                Page<UserMessageForFans> messageOnFans = blogCommitService.getMessageOnFans(Integer.valueOf(currentpage.toString()), Integer.valueOf(currentsize.toString()), uid);
                if(messageOnFans != null){
                    blogHttpUtils.RefreshCookie(request,response);
                    return MsgResult.add(ResultStatus.success).add("data",messageOnFans.getRecords()).add("total",messageOnFans.getTotal());
                }
            }

        }
        return  MsgResult.add(ResultStatus.failure);
    }

    @PostMapping("/admin/list")
    public MsgResult adminCommitList(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(currpage != null &&cookieUid!=null && pagesize != null){
            String decrypt = rsaService.decrypt(cookieUid);
            List<HashMap<String, Object>> articles =blogCommitService.listAdminCommit(decrypt,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null ){
                if(articles.size()==0){
                    return MsgResult.add(ResultStatus.article_commits);
                }
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/getTitleList")
    public MsgResult adminGetTitleList(@RequestBody Map<String,Object> map, HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        Object c_type = map.get("c_type");
        String requestCookieUid = blogHttpUtils.getRequestCookieUid(request);
        if (requestCookieUid != null && c_type !=null) {
            String decrypt = rsaService.decrypt(requestCookieUid);
            List<Map<String,Object>> userList=blogCommitService.getTitleList(decrypt,Integer.valueOf(c_type.toString()));
            if(userList != null){
                return MsgResult.add(ResultStatus.success).add("data",userList);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/change")
    public MsgResult adminCommitChange(@RequestBody AdminCommit adminCommit, HttpServletResponse response, HttpServletRequest request){
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(adminCommit != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            String decrypt = rsaService.decrypt(cookieUid);
            boolean save = blogCommitService.adminCommitChange(adminCommit,decrypt,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/del")
    public MsgResult adminCommitDelete(@RequestBody Map<String,Object> map, HttpServletResponse response, HttpServletRequest request){
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        Object cids = map.get("cids");
        if(cids != null && cookieUid !=null){
            LocalDateTime nowTime = LocalDateTime.now().withNano(0);
            String decrypt = rsaService.decrypt(cookieUid);
            boolean save = blogCommitService.adminCommitDelete(cids.toString(),decrypt,nowTime);
            if(save){
                blogHttpUtils.RefreshCookie(request, response);
                return MsgResult.add( ResultStatus.success);
            }
        }
        return  MsgResult.add(ResultStatus.authorization_admin_change);
    }

    @PostMapping("/admin/search")
    public MsgResult adminCommitSearch(@RequestBody Map<String,Object> map,HttpServletRequest request,HttpServletResponse response){
        BlogHttpUtils blogHttpUtils = new BlogHttpUtils();
        String s_str = (String) map.get("s_str");
        String s_types = (String) map.get("s_types");
        Object currpage = map.get("currpage");
        Object pagesize = map.get("pagesize");
        String cookieUid = blogHttpUtils.getRequestCookieUid(request);
        if(s_str != null && s_types != null && currpage != null &&cookieUid!=null && pagesize != null){
            String decrypt = rsaService.decrypt(cookieUid);
            List<HashMap<String, Object>> articles =blogCommitService.searchMultAdminCommit(s_str,s_types, decrypt,Integer.valueOf(currpage.toString()),Integer.valueOf(pagesize.toString()));
            if(articles != null){
                blogHttpUtils.RefreshCookie(request,response);
                return  MsgResult.add(ResultStatus.success).add("data",articles);
            }
        }
        return  MsgResult.add(ResultStatus.failure);
    }

}
