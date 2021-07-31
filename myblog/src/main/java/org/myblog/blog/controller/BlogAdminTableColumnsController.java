package org.myblog.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.myblog.blog.entity.BlogAdminTableColumns;
import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.BlogAdminTableColumnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author test
 * @since 2021-07-27
 */
@RestController
@RequestMapping("/blog/blog-admin-table-columns")
public class BlogAdminTableColumnsController {

    @Autowired
    BlogAdminTableColumnsService blogAdminTableColumnsService;

    @PostMapping("/save")
    public MsgResult save(@RequestBody List<BlogAdminTableColumns> tableColumnsList){
        if(tableColumnsList !=null){
            for (BlogAdminTableColumns columns : tableColumnsList) {
                boolean save = blogAdminTableColumnsService.save(columns);
                if(!save){
                    return MsgResult.add(ResultStatus.failure).add("msg",columns.getLabel());
                }
            }
        }
        return MsgResult.add(ResultStatus.success);
    }

    @GetMapping("/getColumns")
    public MsgResult getColumns(@PathParam("t") Integer t, HttpServletResponse response, HttpServletRequest request){
        if(t != null){
            QueryWrapper<BlogAdminTableColumns> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ccctype",t);
            List<BlogAdminTableColumns> list = blogAdminTableColumnsService.list(queryWrapper);
            if(list != null){
                return MsgResult.add(ResultStatus.success).add("data",list);
            }
        }

        return MsgResult.add(ResultStatus.failure);
    }



}
