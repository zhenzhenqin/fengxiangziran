package com.fengxiang.controller.admin;

import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.result.PageResult;
import com.fengxiang.result.Result;
import com.fengxiang.service.OrderService;
import com.fengxiang.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单相关接口
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索参数：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id获取订单详情
     * @return
     */
    @ApiOperation("根据id获取订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> getDetailById(@PathVariable Long id){
        log.info("根据id获取订单详情：{}",id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }


    /**
     * 各个状态的订单统计
     */


}
