package com.fengxiang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fengxiang.constant.MessageConstant;
import com.fengxiang.context.BaseContext;
import com.fengxiang.dto.OrdersCancelDTO;
import com.fengxiang.dto.OrdersPageQueryDTO;
import com.fengxiang.dto.OrdersPaymentDTO;
import com.fengxiang.dto.OrdersSubmitDTO;
import com.fengxiang.entity.*;
import com.fengxiang.exception.OrderBusinessException;
import com.fengxiang.mapper.OrderDetailMapper;
import com.fengxiang.mapper.OrderMapper;
import com.fengxiang.mapper.ShoppingCartMapper;
import com.fengxiang.mapper.UserMapper;
import com.fengxiang.result.PageResult;
import com.fengxiang.service.AddressBookService;
import com.fengxiang.service.OrderService;
import com.fengxiang.vo.OrderPaymentVO;
import com.fengxiang.vo.OrderStatisticsVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private UserMapper userMapper;



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

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);


        //根据订单编号查询订单号
        Orders order = orderMapper.getByNumber(ordersPaymentDTO.getOrderNumber());
        log.info("根据订单号查询到的订单信息为：{}", order);
        Long orderId = order.getId();  // 安全地获取订单ID

        /*//调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "蜂享自然订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }*/

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        Integer OrderPaidStatus = Orders.PAID;//支付状态，已支付
        Integer OrderStatus = Orders.TO_BE_SHIPPED;  //订单状态，待发货
        LocalDateTime check_out_time = LocalDateTime.now();//更新支付时间
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderId);

        //webSocket向客户端发送实时订单消息 type, orderId, content
        Map map = new HashMap();
        map.put("type", 1);  //1表示来单提醒
        map.put("orderId", orderId);
        map.put("content", "订单号为：" + order.getNumber());

        String jsonString = JSON.toJSONString(map);

        return vo;

        /*OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;*/
    }

    /**
     * 用户端订单分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param status
     * @return
     */
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入OrderVO进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();// 订单id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 订单退款
     * @param id
     */
    @Override
    public void refund(Long id) {
        Orders order = orderMapper.getById(id);

        //查询订单判断是否存在
        if(order == null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //如果存在 判断状态是否是待发货状态
        if(order.getStatus() > 2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(order.getId());

        //如果是待发货状态，则退款
        if(order.getStatus().equals(Orders.TO_BE_SHIPPED)){
            //调用微信支付退款接口
            /*weChatPayUtil.refund(
                    order.getNumber(), //商户订单号
                    order.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额*/

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、退款原因、退款时间
        orders.setStatus(Orders.REFUND);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void repetition(Long id) {
        //根据订单id查询到订单详情
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(id);

        //将订单对象转化为购物车对象
        //购物车一条商品占据一行
        List<ShoppingCart> carts = orderDetails.stream().map(orderDetail -> {
            ShoppingCart cart = new ShoppingCart();

            //将订单详情拷贝到购物车中
            BeanUtils.copyProperties(orderDetail, cart);
            //设置用户id
            cart.setUserId(BaseContext.getCurrentId());
            cart.setCreateTime(LocalDateTime.now());

            return cart;
        }).collect(Collectors.toList());

        //将购物车数据批量插入到数据库
        shoppingCartMapper.insertBatch(carts);
    }

    /**
     * 统计订单数据
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        //查询待发货数量
        Integer toBeConfirmed = orderMapper.countByStatus(Orders.TO_BE_SHIPPED);
        //查查询运输中数量
        Integer confirmed = orderMapper.countByStatus(Orders.SHIPPED);
        //查询已完成数量
        Integer deliveryInProgress = orderMapper.countByStatus(Orders.COMPLETED);

        //封装vo对象返回前端
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);

        return orderStatisticsVO;
    }

    /**
     * 管理端取消订单
     * @param ordersCancelDTO
     */
    @Override
    public void adminCancel(OrdersCancelDTO ordersCancelDTO) {
        Orders order = orderMapper.getById(ordersCancelDTO.getId()); //根据id查询到订单

        //判断是否已经支付 已经支付退款
        if(order.getPayStatus() == Orders.PAID){
            //weChatPayUtil.refund(order.getNumber(), order.getNumber(), new BigDecimal(0.01), new BigDecimal(0.01));
            order.setPayStatus(Orders.REFUND); //修改订单支付状态为已退款
        }

        //修改订单状态为已取消
        order.setId(ordersCancelDTO.getId());
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason(ordersCancelDTO.getCancelReason());
        order.setCancelTime(LocalDateTime.now());

        orderMapper.update(order);
    }

    /**
     * 派送订单
     * @param id
     */
    @Override
    public void delivery(Long id) {

        //判断订单是否处于已接单状态
        Orders order = orderMapper.getById(id);
        if(order.getStatus() != Orders.TO_BE_SHIPPED){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //修改订单状态为派送中
        order.setStatus(Orders.SHIPPED);
        orderMapper.update(order);
    }

    /**
     * 完成订单
     * @param id
     */
    @Override
    public void complete(Long id) {
        //判断订单是否处于派送中状态
        Orders order = orderMapper.getById(id);
        if(order.getStatus() != Orders.SHIPPED){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //修改订单状态为已完成
        order.setStatus(Orders.COMPLETED);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    /**
     * 管理端订单退款
     * @param id
     */
    @Override
    public void adminRefund(Long id) {
        //修改订单状态为退款
        Orders order = orderMapper.getById(id);
        order.setStatus(Orders.REFUND);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(order);
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
