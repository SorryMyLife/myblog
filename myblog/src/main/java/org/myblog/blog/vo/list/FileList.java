package org.myblog.blog.vo.list;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileList {
    public LocalDateTime createTime;
    public Integer uid,fid,commits;
    public String icon,fname,tags,text,dlink,html;
    public Long size;
}
