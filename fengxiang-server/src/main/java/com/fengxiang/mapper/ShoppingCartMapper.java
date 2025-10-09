package com.fengxiang.mapper;

import com.fengxiang.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 根据用户id和商品id查询购物车数据
     * @param shoppingCart
     * @return
     */
    ShoppingCart getByUserIdAndGoodId(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param cart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart cart);

    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, image, user_id, good_id, number, amount, create_time) " +
            "values (#{name}, #{image}, #{userId}, #{goodId}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id查询购物车数据
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车数据
     * @param currentId
     */
    @Delete("delete from shopping_cart where user_id = #{id}")
    void deleteByUserId(Long id);
}
