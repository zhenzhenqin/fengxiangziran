package com.fengxiang.service.impl;

import com.fengxiang.constant.MessageConstant;
import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.entity.OrderDetail;
import com.fengxiang.entity.Orders;
import com.fengxiang.exception.OrderBusinessException;
import com.fengxiang.mapper.OrderDetailMapper;
import com.fengxiang.mapper.OrderMapper;
import com.fengxiang.result.PageResult;
import com.fengxiang.service.OrderService;
import com.fengxiang.vo.OrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 商户端订单搜索
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        //分页查询得到订单列表
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        //获取订单中的商品详情信息 部分订单需要返回商品详情
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    /**
     * 根据id获取
     * @param id
     * @return
     */
    @Override
    public OrderVO details(Long id) {
        //根据id查询订单
        Orders order = orderMapper.getById(id);

        //如果订单不存在 则抛出异常
        if(order == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //如果订单存在 去获取订单的详细信息
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        //封装vo返回前端
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }





    //此处为方法
    /**
     * 获取订单中的商品详情信息
     * @param page
     * @return
     */
    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> result = page.getResult();
        //查询到的订单不为空
        if(result != null && result.size() > 0){
            for(Orders order : result){
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                String orderGoods = getOrderGoodsStr(order);

                orderVO.setOrderGoods(orderGoods);
                orderVOList.add(orderVO);
            }
        }

        return orderVOList;
    }

    //对订单中的商品进行字符串处理
    private String getOrderGoodsStr(Orders order) {
        Long orderId = order.getId(); //获取订单id
        //通过订单id获取订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);
        //遍历数组取出里面的菜品详情
        StringBuilder orderGoodsStr = new StringBuilder();
        if (!CollectionUtils.isEmpty(orderDetails)){
            for(OrderDetail orderDetail : orderDetails){
                orderGoodsStr.append(orderDetail.getName()).append("*").append(orderDetail.getNumber()).append(";");
            }
        }
        return orderGoodsStr.toString();
    }
}
