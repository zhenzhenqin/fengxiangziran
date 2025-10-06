package com.fengxiang.mapper;

import com.fengxiang.annotation.AutoFill;
import com.fengxiang.dto.GoodPageQueryDTO;
import com.fengxiang.entity.Good;
import com.fengxiang.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodMapper {

    /**
     * 分页查询商品
     * @param goodPageQueryDTO
     * @return
     */
    List<Good> pageQuery(GoodPageQueryDTO goodPageQueryDTO);


    /**
     * 新增商品
     * @param good
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Good good);

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @Select("select * from good where id = #{id}")
    Good getById(Long id);

    /**
     * 修改商品
     * @param good
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Good good);

    /**
     * 查询商品根据分类id
     * @param good
     * @return
     */
    List<Good> list(Good good);

    /**
     * 批量删除商品
     * @param ids
     */
    void deleteById(List<Long> ids);
}
