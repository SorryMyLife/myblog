package org.myblog.blog.vo.admin;

import lombok.Data;
import org.myblog.blog.entity.BlogArticle;

@Data
public class AdminArticle extends BlogArticle {
    public String uname,roleName;
    public String[] tagsName;
    public Integer commits;
}
