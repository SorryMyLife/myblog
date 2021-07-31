package org.myblog.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 发布者id
     */
    private Integer uid;

    /**
     * 文章内容
     */
    private String text;

    /**
     * 文章标签
     */
    private String tags;

    /**
     * 文章发布时间
     */
    private LocalDateTime createTime;

    /**
     * 谁修改了这个文章
     */
    private Integer changeRole;

    /**
     * 文章修改时间
     */
    private LocalDateTime changeTime;

    /**
     * 文章内容html
     */
    private String html;

    /**
     * 被观看次数
     */
    private Integer watch;


}
