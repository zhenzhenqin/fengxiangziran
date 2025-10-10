package com.fengxiang.mapper;

import com.fengxiang.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper {

    /**
     * 新增地址
     * @param addressBook
     */
    @Insert("insert into address_book (user_id, consignee, sex, phone, province_code, province_name, " +
            "city_code, city_name, district_code, district_name, detail, label) values (#{userId}, " +
            "#{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, " +
            "#{districtCode}, #{districtName}, #{detail}, #{label})")
    void insert(AddressBook addressBook);
}
