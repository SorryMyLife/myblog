package org.myblog.blog.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMessageForFans {
    public LocalDateTime createTime;
    public Integer uid;
    public String icon,name;

}
