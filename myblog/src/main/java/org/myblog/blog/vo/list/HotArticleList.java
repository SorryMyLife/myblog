package org.myblog.blog.vo.list;

import lombok.Data;
import org.myblog.blog.entity.BlogArticle;

@Data
public class HotArticleList extends BlogArticle {
    public Integer commits,likes;
}
