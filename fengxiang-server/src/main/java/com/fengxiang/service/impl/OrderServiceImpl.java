package com.fengxiang.service.impl;

import com.fengxiang.constant.MessageConstant;
import com.fengxiang.context.BaseContext;
import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.dto.OrdersSubmitDTO;
import com.fengxiang.entity.AddressBook;
import com.fengxiang.entity.OrderDetail;
import com.fengxiang.entity.Orders;
import com.fengxiang.entity.ShoppingCart;
import com.fengxiang.exception.OrderBusinessException;
import com.fengxiang.mapper.OrderDetailMapper;
import com.fengxiang.mapper.OrderMapper;
import com.fengxiang.mapper.ShoppingCartMapper;
import com.fengxiang.result.PageResult;
import com.fengxiang.service.AddressBookService;
import com.fengxiang.service.OrderService;
import com.fengxiang.vo.OrderSubmitVO;
import com.fengxiang.vo.OrderVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

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

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种业务异常
        //通过id获得地址簿信息
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookService.getAddressById(addressBookId);
        //如果地址簿为空 则抛出异常
        if(addressBook == null){
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        //根据当前id获取当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        //判断当前购物车数据是否为空 如果为空 则抛出异常
        if(CollectionUtils.isEmpty(shoppingCartList)){
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis())); //订单编号 根据当前时间戳生成
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        orderMapper.insert(orders); //将订单插入数据库订单表


        //向订单明细表插入n条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(ShoppingCart cart : shoppingCartList){
            OrderDetail orderDetail = new OrderDetail(); //订单明细对象
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId()); //设置订单用户对象
            orderDetailList.add(orderDetail); //将一条订单明细插入列表中
        }
        orderDetailMapper.insertBatch(orderDetailList); //将订单列表批量插入数据库订单明细表

        //插入成功后清空购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        //返回对应的vo数据
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();


        return orderSubmitVO;
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
