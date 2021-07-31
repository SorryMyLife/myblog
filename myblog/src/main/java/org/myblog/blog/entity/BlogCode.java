package org.myblog.blog.entity;

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
public class BlogCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求验证码时产生的唯一id
     */
    private String cid;

    /**
     * 需要进行验证的用户id
     */
    private String uid;

    /**
     * 返回的验证码内容
     */
    private String code;


}
