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
public class BlogRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色创建时间
     */
    private LocalDateTime createTime;

    /**
     * 谁创建的
     */
    private Integer createRole;

    /**
     * 角色修改时间
     */
    private LocalDateTime changeTime;

    /**
     * 谁修改了这个角色
     */
    private Integer changeRole;


}
