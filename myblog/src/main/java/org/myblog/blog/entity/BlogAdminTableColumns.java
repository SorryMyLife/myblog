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
 * @since 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BlogAdminTableColumns implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 后台管理字段
     */
    private String prop;

    /**
     * 后台管理名称
     */
    private String label;

    /**
     * 后台管理元素宽度
     */
    private Integer width;

    /**
     * 后台管理类型
     */
    private Integer ccctype;


}
