package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogLogin;

@Data
public class AdminLoginList extends BlogLogin {

    public String uname,roleName,croleName;

}
