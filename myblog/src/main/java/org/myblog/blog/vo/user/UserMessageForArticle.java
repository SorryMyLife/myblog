package org.myblog.blog.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMessageForArticle {
    public LocalDateTime createTime;
    public Integer uid,aid;
    public String icon,name,msg,title;
}
