package org.myblog.blog.service;

import org.myblog.blog.entity.AdministrativeDivisionCode;
import com.baomidou.mybatisplus.extension.service.IService;
import org.myblog.blog.vo.list.DivisionList;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface AdministrativeDivisionCodeService extends IService<AdministrativeDivisionCode> {

    public String getDivisionName(String address);

    List<DivisionList> getDivisionList();



}
