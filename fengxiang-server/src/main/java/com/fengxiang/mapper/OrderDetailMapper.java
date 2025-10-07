package com.fengxiang.mapper;

import com.fengxiang.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderDetailMapper {

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);

    /**
     * 查询销量排名top10
     * @param map
     * @return
     */
    List<OrderDetail> getSalesTop10(Map map);
}
