package com.fengxiang.service.impl;

import com.fengxiang.constant.MessageConstant;
import com.fengxiang.constant.StatusConstant;
import com.fengxiang.dto.GoodDTO;
import com.fengxiang.dto.GoodPageQueryDTO;
import com.fengxiang.entity.Good;
import com.fengxiang.exception.DeletionNotAllowedException;
import com.fengxiang.mapper.GoodMapper;
import com.fengxiang.result.PageResult;
import com.fengxiang.result.Result;
import com.fengxiang.service.GoodService;
import com.fengxiang.vo.GoodVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodMapper goodMapper;

    /**
     *  分页查询商品
     * @param goodPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(GoodPageQueryDTO goodPageQueryDTO) {
        PageHelper.startPage(goodPageQueryDTO.getPage(), goodPageQueryDTO.getPageSize());
        List<Good> list = goodMapper.pageQuery(goodPageQueryDTO);
        Page<Good> page = (Page<Good>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 新增商品
     * @param goodDTO
     */
    @Override
    public void save(GoodDTO goodDTO) {
        Good good = new Good();
        BeanUtils.copyProperties(goodDTO, good);
        good.setStatus(StatusConstant.DISABLE);
        goodMapper.insert(good);
    }

    /**
     *  根据id查询商品
     * @param id
     * @return
     */
    @Override
    public GoodVO getById(Long id) {
        Good good = goodMapper.getById(id);
        GoodVO goodVO = new GoodVO();
        BeanUtils.copyProperties(good, goodVO);
        return goodVO;
    }

    /**
     * 修改商品
     * @param goodDTO
     */
    @Override
    public void update(GoodDTO goodDTO) {
        Good good = new Good();
        BeanUtils.copyProperties(goodDTO, good);
        goodMapper.update(good);
    }

    /**
     * 查询商品根据分类id
     * @param categoryId
     * @return
     */
    @Override
    public List<Good> list(Long categoryId) {
        Good good = Good.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return goodMapper.list(good);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否在起售中
        for (Long id : ids) {
            Good good = goodMapper.getById(id);
            if (good.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.GOOD_ON_SALE);
            }
        }

        //删除菜品数据
        goodMapper.deleteById(ids);
    }

    /**
     * 起售停售商品
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Good good = Good.builder()
                .id(id)
                .status(status)
                .build();

        goodMapper.update(good);
    }

}
