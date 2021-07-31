package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.myblog.blog.entity.AdministrativeDivisionCode;
import org.myblog.blog.mapper.AdministrativeDivisionCodeMapper;
import org.myblog.blog.service.AdministrativeDivisionCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.vo.list.DivisionList;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Service
public class AdministrativeDivisionCodeServiceImpl extends ServiceImpl<AdministrativeDivisionCodeMapper, AdministrativeDivisionCode> implements AdministrativeDivisionCodeService {

    @Override
    public String getDivisionName(String address) {
        String[] split = address.split(",");
        if (split != null) {
            LambdaQueryWrapper<AdministrativeDivisionCode> lambdaQuery = Wrappers.<AdministrativeDivisionCode>lambdaQuery();
            if(split.length ==3){
                lambdaQuery.eq(AdministrativeDivisionCode::getProvince,split[0]).eq(AdministrativeDivisionCode::getCity,split[1]).eq(AdministrativeDivisionCode::getCounty,split[2]);
            }
            if(split.length ==2){
                lambdaQuery.eq(AdministrativeDivisionCode::getProvince,split[0]).eq(AdministrativeDivisionCode::getCounty,split[1]);
            }
            AdministrativeDivisionCode divisionCode = this.getOne(lambdaQuery);
            return divisionCode.getProvinceName()+divisionCode.getCityName()+divisionCode.getCountyName();
             }
        return null;
       }

    @Override
    public List<DivisionList> getDivisionList() {
        List<DivisionList> list =this.getBaseMapper().getProvinceList();
        for (DivisionList divisionList : list) {
            if(divisionList.getValue().equals("110000") || divisionList.getValue().equals("120000") || divisionList.getValue().equals("500000") || divisionList.getValue().equals("310000")){
                List<DivisionList> countyList = this.getBaseMapper().getCountyList(divisionList.getValue());
                divisionList.setChildren(countyList);
            }else{
                List<DivisionList> cityList = this.getBaseMapper().getCityList(divisionList.getValue());
                for (DivisionList divisionList1 : cityList) {
                    List<DivisionList> countyList = this.getBaseMapper().getCountyList(divisionList1.getValue());
                    divisionList1.setChildren(countyList);
                }
                divisionList.setChildren(cityList);
            }

        }
        return list;
    }
}
