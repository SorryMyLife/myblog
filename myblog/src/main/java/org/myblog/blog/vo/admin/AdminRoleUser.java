package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogAdminUser;

@Data
public class AdminRoleUser extends BlogAdminUser {
    public String uname,rname,roleName;
}
