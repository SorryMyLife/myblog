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
public class BlogRsa implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 加密私匙
     */
    private String privateKey;

    /**
     * 加密公匙
     */
    private String publicKey;


}
