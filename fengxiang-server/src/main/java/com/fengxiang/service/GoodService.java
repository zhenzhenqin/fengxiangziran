package com.fengxiang.service;

import com.fengxiang.dto.GoodDTO;
import com.fengxiang.dto.GoodPageQueryDTO;
import com.fengxiang.entity.Good;
import com.fengxiang.result.PageResult;
import com.fengxiang.vo.GoodVO;

import java.util.List;

public interface GoodService {

    /**
     * 商品分页查询
     * @param goodPageQueryDTO
     * @return
     */
    PageResult pageQuery(GoodPageQueryDTO goodPageQueryDTO);

    /**
     * 新增商品
     * @param goodDTO
     */
    void save(GoodDTO goodDTO);

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    GoodVO getById(Long id);

    /**
     *  修改商品
     * @param goodDTO
     */
    void update(GoodDTO goodDTO);

    /**
     * 根据分类id查询
     * @param categoryId
     * @return
     */
    List<Good> list(Long categoryId);

    /**
     * 批量删除商品
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 起售停售商品
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类id查询商品
     * @param good
     * @return
     */
    List<GoodVO> getByCategoryId(Good good);
}
