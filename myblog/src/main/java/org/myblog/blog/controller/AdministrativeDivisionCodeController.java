package org.myblog.blog.controller;


import org.myblog.blog.extend.MsgResult;
import org.myblog.blog.extend.ResultStatus;
import org.myblog.blog.service.AdministrativeDivisionCodeService;
import org.myblog.blog.vo.list.DivisionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@RestController
@RequestMapping("/blog/administrative-division-code")
public class AdministrativeDivisionCodeController {

    @Autowired
    AdministrativeDivisionCodeService administrativeDivisionCodeService;

    @GetMapping("/getDivision")
    public MsgResult getDivision(){
        List<DivisionList> divisionLists= administrativeDivisionCodeService.getDivisionList();
        if(divisionLists != null){
            return MsgResult.add(ResultStatus.success).add("data",divisionLists);
        }
        return MsgResult.add(ResultStatus.failure);
    }

}
