package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogCode;

@Data
public class AdminCodeList extends BlogCode {
    public String uname,roleName,croleName;

}
