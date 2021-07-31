package org.myblog.blog.vo.user;

import lombok.Data;
import org.myblog.blog.entity.BlogUser;

@Data
public class UserInfo {
    public Integer fans,commits,articles,files,likes,watch,gender;
    public String name , id,icon,address,autograph,email,phone;
}
