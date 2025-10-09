package com.fengxiang.service.impl;

import com.fengxiang.context.BaseContext;
import com.fengxiang.dto.ShoppingCartDTO;
import com.fengxiang.entity.Good;
import com.fengxiang.entity.ShoppingCart;
import com.fengxiang.mapper.GoodMapper;
import com.fengxiang.mapper.ShoppingCartMapper;
import com.fengxiang.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private GoodMapper goodMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        Long currentId = BaseContext.getCurrentId();

        // 增加用户ID空值检查
        if (currentId == null) {
            throw new RuntimeException("用户未登录，无法添加商品到购物车");
        }

        shoppingCart.setUserId(currentId);   //设置用户id

        //从数据库表中查询是否存在该商品
        ShoppingCart cart = shoppingCartMapper.getByUserIdAndGoodId(shoppingCart);
        if(cart != null){
            //数据库存在此商品，商品加1
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.update(cart);
        } else{
            //获取商品id
            Long goodId = shoppingCart.getGoodId();

            //根据id获取商品信息
            if(goodId != null){
                Good good = goodMapper.getById(goodId);
                //获取商品
                shoppingCart.setName(good.getName()); //设置商品名称
                shoppingCart.setImage(good.getImage()); //设置商品图片
                shoppingCart.setAmount(good.getPrice()); //设置商品价格
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 获取购物车
     * @return
     */
    @Override
    public List<ShoppingCart> list() {
        //根据userid查询购物车
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(currentId)
                .build();

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        return shoppingCartList;
    }

    /**
     * 清空购物车
     */
    @Override
    public void clean() {
        //根据当前用户id删除购物车
        Long id = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(id);
    }
}
