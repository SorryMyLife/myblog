package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogUser;

@Data
public class AdminUser extends BlogUser {
    public String[] addr;
    public String address2,roleName,croleName;
    public Integer role_name;
}
