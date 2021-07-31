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
public class BlogFans implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 用户粉丝id
     */
    private Integer fansId;

    /**
     * 成为用户粉丝时间
     */
    private LocalDateTime createTime;

    /**
     * 粉丝变动时间
     */
    private LocalDateTime changeTime;

    /**
     * 谁变动的
     */
    private Integer changeRole;


}
