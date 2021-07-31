package org.myblog.blog.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.myblog.blog.entity.AdministrativeDivisionCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.myblog.blog.vo.list.DivisionList;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface AdministrativeDivisionCodeMapper extends BaseMapper<AdministrativeDivisionCode> {

    @Select("select adc.province_name as label,adc.province as value from administrative_division_code as adc  where adc.province=adc.city and adc.province=adc.county GROUP BY adc.province_name,adc.province")
    List<DivisionList> getProvinceList();


    @Select("select adc.city_name as label,adc.city as value from administrative_division_code as adc  where adc.province!=adc.city and adc.province=\"${province}\" GROUP BY adc.city_name,adc.city")
    List<DivisionList> getCityList(@Param("province") String province);

    @Select("select adc.county_name as label,adc.county as value from administrative_division_code as adc  where adc.city!=adc.county and adc.city=\"${city}\" GROUP BY adc.county_name,adc.county")
    List<DivisionList> getCountyList(@Param("city") String city);
}
