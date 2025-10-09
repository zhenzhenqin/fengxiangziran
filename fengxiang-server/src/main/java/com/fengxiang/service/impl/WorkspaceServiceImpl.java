package com.fengxiang.service.impl;

import com.fengxiang.entity.Good;
import com.fengxiang.entity.Orders;
import com.fengxiang.mapper.GoodMapper;
import com.fengxiang.mapper.OrderMapper;
import com.fengxiang.mapper.UserMapper;
import com.fengxiang.service.WorkspaceService;
import com.fengxiang.vo.BusinessDataVO;
import com.fengxiang.vo.GoodOverViewVO;
import com.fengxiang.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoodMapper goodMapper;

    private final LocalDateTime beginTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    private final LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

    /**
     * 获得今日运营数据
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {

        Map map = new HashMap<>();
        map.put("status",Orders.COMPLETED);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);

        //获取新增用户数
        Map map1 = new HashMap<>(); //特殊情况 如不需要传入状态的情况
        map1.put("beginTime", beginTime);
        map1.put("endTime", endTime);
        Integer newUser = userMapper.getUserByDateAndStatus(map1);
        if (newUser == null){
            newUser = 0;
        }

        //获取有效订单数
        Integer validOrderCount = orderMapper.getOrdersNumberByDateAndStatus(map); //获取有效订单数
        if(validOrderCount == null){
            validOrderCount = 0;
        }

        //获取订单完成率
        Integer orderCount = orderMapper.getOrdersNumberByDateAndStatus(map1); //获取订单总数
        Double orderCompleteRate = 0.0;
        if(orderCount != 0){
            orderCompleteRate = validOrderCount / (double)orderCount;//订单完成率
        }


        //获取营业额
        Double turnover = orderMapper.getByDateAndStatus(map);
        if(turnover == null){
            turnover = 0.0;
        }

        //获取平均客单价
        Double avgOrderPrice = 0.0;
        if(validOrderCount != 0){
            avgOrderPrice = turnover / validOrderCount;
        }


        //封装vo返回
        return BusinessDataVO.builder()
                .newUsers(newUser)
                .orderCompletionRate(orderCompleteRate)
                .turnover( turnover)
                .unitPrice(avgOrderPrice)
                .validOrderCount(validOrderCount)
                .build();
    }

    /**
     * 获得订单统计数据
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverView() {
        //对于没有订单状况查询的处理
        Map map = new HashMap<>();
        /*map.put("beginTime", beginTime);
        map.put("endTime", endTime);*/

        //返回全部订单
        Integer allOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取订单总数
        if (allOrdersNumber == null){
            allOrdersNumber = 0;
        }

        //返回已取消数量
        map.put("status",Orders.CANCELLED);
        Integer cancelledOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取已取消订单数
        if (cancelledOrdersNumber == null){
            cancelledOrdersNumber = 0;
        }

        //返回已完成数量
        map.put("status",Orders.COMPLETED);
        Integer completedOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取已完成订单数
        if (completedOrdersNumber == null){
            completedOrdersNumber = 0;
        }

        //返回已发货数量
        map.put("status",Orders.SHIPPED);
        Integer deliveryInProgressOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取待派送订单数
        if (deliveryInProgressOrdersNumber == null){
            deliveryInProgressOrdersNumber = 0;
        }

        //返回待发货数量
        map.put("status",Orders.TO_BE_SHIPPED);
        Integer toBeShippedOrdersNumber = orderMapper.getOrdersNumberByDateAndStatus(map); //获取待发货订单数
        if (toBeShippedOrdersNumber == null){
            toBeShippedOrdersNumber = 0;
        }

        //封装vo返回
        return OrderOverViewVO.builder()
                .allOrders(allOrdersNumber)
                .cancelledOrders(cancelledOrdersNumber)
                .completedOrders(completedOrdersNumber)
                .shippedOrders(deliveryInProgressOrdersNumber)
                .toBeShippedOrders(toBeShippedOrdersNumber)
                .build();
    }


    /**
     * 获得商品统计数据
     * @return
     */
    @Override
    public GoodOverViewVO getGoodOverView() {
        //已停售商品数量
        Good good = new Good();
        good.setStatus(0);

        int stopNumber = goodMapper.list(good).size();

        //已起售商品数量
        good.setStatus(1);
        int startNumber = goodMapper.list(good).size();

        //封装vo返回
        return GoodOverViewVO.builder()
                .discontinued(stopNumber)
                .sold(startNumber)
                .build();
    }


    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData30(LocalDateTime begin, LocalDateTime end) {

        Map map = new HashMap<>();
        map.put("status",Orders.COMPLETED);
        map.put("beginTime", begin);
        map.put("endTime", end);

        //获取新增用户数
        Map map1 = new HashMap<>(); //特殊情况 如不需要传入状态的情况
        map1.put("beginTime", begin);
        map1.put("endTime", end);
        Integer newUser = userMapper.getUserByDateAndStatus(map1);
        if (newUser == null){
            newUser = 0;
        }

        //获取有效订单数
        Integer validOrderCount = orderMapper.getOrdersNumberByDateAndStatus(map); //获取有效订单数
        if(validOrderCount == null){
            validOrderCount = 0;
        }

        //获取订单完成率
        Integer orderCount = orderMapper.getOrdersNumberByDateAndStatus(map1); //获取订单总数
        Double orderCompleteRate = 0.0;

        if(orderCount != 0){
            orderCompleteRate = validOrderCount / (double)orderCount; //订单完成率
        }

        //获取营业额
        Double turnover = orderMapper.getByDateAndStatus(map);  //获取当日营业额
        if(turnover == null){
            turnover = 0.0;
        }

        //获取平均客单价
        //获取平均客单价
        Double avgOrderPrice = 0.0;
        if(validOrderCount != 0){
            avgOrderPrice = turnover / validOrderCount;
        }


        //封装vo返回
        return BusinessDataVO.builder()
                .newUsers(newUser)
                .orderCompletionRate(orderCompleteRate)
                .turnover( turnover)
                .unitPrice(avgOrderPrice)
                .validOrderCount(validOrderCount)
                .build();
    }
}
