package org.myblog.blog.vo.admin;

import lombok.Data;

@Data
public class AdminPwd {
    public Integer uid;
    public String oldpwd,newpwd1,newpwd2;
}
