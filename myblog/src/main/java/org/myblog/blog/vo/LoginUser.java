package org.myblog.blog.vo;
import lombok.Data;

@Data
public class LoginUser {
    public String id,password,code;
    public Integer loginType;
}
