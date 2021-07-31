package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogFile;

@Data
public class AdminFile extends BlogFile {
    public String uname,roleName;
    public String[] tagsName;
    public Integer commits;
}
