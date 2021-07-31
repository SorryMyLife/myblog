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
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 错误登录次数
     */
    private Integer loginErrorCount;

    /**
     * 用户登录ip位置
     */
    private String ipAddr;

    /**
     * 登录时间
     */
    private LocalDateTime createTime;
    private LocalDateTime changeTime;
    private Integer createRole;
    private Integer changeRole;


    /**
     * 逻辑删除(0:不删除/1:删除)
     */
    private Integer isDel;

    /**
     * 用户登录次数
     */
    private Integer loginCount;


}
