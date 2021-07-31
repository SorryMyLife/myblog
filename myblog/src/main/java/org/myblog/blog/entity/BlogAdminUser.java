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
 * @since 2021-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogAdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 角色id
     */
    private Integer rid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者id
     */
    private Integer createRole;

    /**
     * 修改时间
     */
    private LocalDateTime changeTime;

    /**
     * 修改者id
     */
    private Integer changeRole;


}
