package com.fengxiang.service;

import com.fengxiang.dto.OrdersCancelDTO;
import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.dto.OrdersPaymentDTO;
import com.fengxiang.dto.OrdersSubmitDTO;
import com.fengxiang.result.PageResult;
import com.fengxiang.vo.OrderPaymentVO;
import com.fengxiang.vo.OrderStatisticsVO;
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

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);

    /**
     * 查看历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 订单退款
     * @param id
     */
    void refund(Long id);

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * 获取订单统计数据
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 管理端取消订单
     * @param ordersCancelDTO
     */
    void adminCancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单派送
     * @param id
     */
    void delivery(Long id);

    /**
     * 订单完成
     * @param id
     */
    void complete(Long id);

    /**
     * 管理端订单退款
     * @param id
     */
    void adminRefund(Long id);
}
