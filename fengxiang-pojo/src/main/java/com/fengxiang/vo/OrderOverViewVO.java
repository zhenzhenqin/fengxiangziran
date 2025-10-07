package com.fengxiang.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单概览数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverViewVO implements Serializable {

    //订单状态 1待付款 2待发货 3已发货 4已完成 5已取消 6退款

    //待发货数量
    private Integer toBeShippedOrders;

    //已发货数量
    private Integer shippedOrders;

    //已完成数量
    private Integer completedOrders;

    //已取消数量
    private Integer cancelledOrders;

    //全部订单
    private Integer allOrders;
}
