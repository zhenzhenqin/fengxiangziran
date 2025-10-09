package com.fengxiang.service;

import com.fengxiang.dto.CategoryDTO;
import com.fengxiang.dto.CategoryPageQueryDTO;
import com.fengxiang.entity.Category;
import com.fengxiang.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分页查询分类
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQuery);

    /**
     * 修改分类
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 删除分类
     * @param id
     */
    void delete(Long id);

    /**
     * 查询分类
     * @return
     */
    List<Category> getCategoryList();
}
