package org.myblog.blog.entity;

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
public class BlogCommit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论目标ID
     */
    private Integer id;

    /**
     * 评论内容
     */
    private String msg;

    /**
     * 评论用户
     */
    private Integer uid;

    /**
     * 评论时由用户打的标签内容
     */
    private String tags;

    /**
     * 目标ID类型(0为文章,1为文件)
     */
    private Integer type;

    /**
     * 评论发布时间
     */
    private LocalDateTime createTime;

    /**
     * 谁修改的评论
     */
    private Integer changeRole;

    /**
     * 评论修改时间
     */
    private LocalDateTime chagneTime;

    private String html;

}
