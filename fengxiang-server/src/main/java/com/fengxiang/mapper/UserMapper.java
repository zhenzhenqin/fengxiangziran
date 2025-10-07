package com.fengxiang.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据日期和订单状态查询用户数量
     * @param map1
     * @return
     */
    Integer getUserByDateAndStatus(Map map1);
}
