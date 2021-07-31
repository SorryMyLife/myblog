package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogVerif;

@Data
public class AdminVerifList extends BlogVerif {
    public String uname,roleName,croleName;

}
