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
public class BlogLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 收藏者ID
     */
    private Integer likeId;

    /**
     * 什么时候收藏的
     */
    private LocalDateTime createTime;

    /**
     * 什么时候修改收藏时间
     */
    private LocalDateTime changeTime;

    /**
     * 谁修改的
     */
    private Integer changeRole;


}
