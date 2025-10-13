package com.fengxiang.mapper;

import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.entity.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
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

    /**
     * 修改订单状态
     * @param orderStatus
     * @param orderPaidStatus
     * @param checkOutTime
     * @param id
     */
    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{checkOutTime} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime checkOutTime, Long id);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 查询订单状态
     * @param toBeShipped
     * @return
     */
    @Select("select count(*) from orders where status = #{orderStatus}")
    Integer countByStatus(Integer toBeShipped);
}
