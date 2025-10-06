package com.fengxiang.service.impl;

import com.fengxiang.constant.MessageConstant;
import com.fengxiang.constant.StatusConstant;
import com.fengxiang.dto.CategoryDTO;
import com.fengxiang.dto.CategoryPageQueryDTO;
import com.fengxiang.entity.Category;
import com.fengxiang.entity.Good;
import com.fengxiang.exception.DeletionNotAllowedException;
import com.fengxiang.mapper.CategoryMapper;
import com.fengxiang.mapper.GoodMapper;
import com.fengxiang.result.PageResult;
import com.fengxiang.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private GoodMapper goodMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category); //属性拷贝
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.insert(category);
    }

    /**
     * 分页查询分类
     * @param categoryPageQuery
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQuery) {
        PageHelper.startPage(categoryPageQuery.getPage(), categoryPageQuery.getPageSize());
        List<Category> categoryList = categoryMapper.pageQuery(categoryPageQuery);
        Page<Category> page = (Page<Category>) categoryList;
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();

        categoryMapper.update(category);
    }

    /**
     * 删除分类
     * @param id
     */
    @Override
    public void delete(Long id) {
        //根据id查询分类
        Category category = categoryMapper.getById(id);
        //判断是否起售 如果处于起售状态则无法删除
        if(category.getStatus() == StatusConstant.ENABLE){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_ON_SALE);
        }

        //判断当前分类是否关联了商品 如果关联无法删除
        //根据商品分类id去查询商品 如果能查到则抛出异常无法删除
        Good good = new Good();
        good.setCategoryId(id);
        List<Good> list = goodMapper.list(good);
        if(list != null && list.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_GOOD);
        }
        categoryMapper.delete(id);
    }
}
