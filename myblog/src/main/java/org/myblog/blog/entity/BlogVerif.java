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
 * @since 2021-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogVerif implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 滑块位置
     */
    private String sliderX;
    private String cid;

}
