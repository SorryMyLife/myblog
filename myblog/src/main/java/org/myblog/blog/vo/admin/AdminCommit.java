package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogCommit;

@Data
public class AdminCommit extends BlogCommit {
    public String uname,roleName,title,typeName;
    public String[] tagsName;
}
