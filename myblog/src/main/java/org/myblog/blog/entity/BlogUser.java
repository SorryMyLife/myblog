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
public class BlogUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户签名
     */
    private String autograph;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 用户登录方式(0为id,1为邮箱,2为手机号)
     */
    private Integer loginType;

    /**
     * 用户创建时间
     */
    private LocalDateTime createTime;

    /**
     * 谁创建的此用户
     */
    private Integer createRole;

    /**
     * 用户修改时间
     */
    private LocalDateTime changeTime;

    /**
     * 谁修改的此用户
     */
    private Integer changeRole;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户头像base64编码后
     */
    private String icon;

    /**
     * 被多少人浏览过
     */
    private Integer watch;


}
