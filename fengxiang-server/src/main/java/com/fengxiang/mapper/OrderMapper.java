package com.fengxiang.mapper;

import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.entity.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 分页查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 根据日期和状态查询营业额
     * @param map
     * @return
     */
    Double getByDateAndStatus(Map map);

    /**
     * 根据日期范围和订单状态查询订单数量
     * @param map
     * @return
     */
    Integer getOrdersNumberByDateAndStatus(Map map);

    /**
     * 插入订单
     * @param orders
     */
    void insert(Orders orders);
}
