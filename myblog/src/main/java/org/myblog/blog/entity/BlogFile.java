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
public class BlogFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件介绍
     */
    private String text;

    /**
     * 文件标签
     */
    private String tags;

    /**
     * 上传者id
     */
    private Integer uid;

    /**
     * 文件下载链接
     */
    private String dlink;

    /**
     * 文件创建时间
     */
    private LocalDateTime createTime;

    /**
     * 谁修改的这个文件内容
     */
    private Integer changeRole;

    /**
     * 文件修改时间
     */
    private LocalDateTime changeTime;

    /**
     * 文件大小
     */
    private Long size;

    private String html;


}
