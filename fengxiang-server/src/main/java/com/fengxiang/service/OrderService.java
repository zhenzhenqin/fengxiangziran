package com.fengxiang.service;

import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.dto.OrdersSubmitDTO;
import com.fengxiang.result.PageResult;
import com.fengxiang.vo.OrderSubmitVO;
import com.fengxiang.vo.OrderVO;

public interface OrderService {

    /**
     * 订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id获取订单详情
     * @param id
     * @return
     */
    OrderVO details(Long id);

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}
