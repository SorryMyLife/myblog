package org.myblog.blog.vo.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMessageForFile {
    public LocalDateTime createTime;
    public Integer uid,fid;
    public String icon,name,msg,dlink;
    public Long size;
}
