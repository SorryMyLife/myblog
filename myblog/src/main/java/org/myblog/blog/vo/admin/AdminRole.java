package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogRole;

@Data
public class AdminRole extends BlogRole {
    public String roleName,croleName;
}
