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
public class AdministrativeDivisionCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 省级行政代码
     */
    private String province;

    /**
     * 省级行政名称
     */
    private String provinceName;

    /**
     * 市级行政代码
     */
    private String city;

    /**
     * 市级行政名称
     */
    private String cityName;

    /**
     * 区县级行政代码
     */
    private String county;

    /**
     * 区县级行政名称
     */
    private String countyName;


}
