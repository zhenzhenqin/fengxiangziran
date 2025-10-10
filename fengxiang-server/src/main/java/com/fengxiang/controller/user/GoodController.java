package com.fengxiang.controller.user;

import com.fengxiang.constant.StatusConstant;
import com.fengxiang.entity.Good;
import com.fengxiang.result.Result;
import com.fengxiang.service.GoodService;
import com.fengxiang.vo.GoodVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "C端-用户端商品接口")
@RestController("goodController")
@RequestMapping("/user/dish")
public class GoodController {

    @Autowired
    private GoodService goodService;

    /**
     * 根据分类id查询商品
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询商品")
    @GetMapping("/list")
    public Result<List<GoodVO>> getGoodByCategoryId(Long categoryId){
        log.info("根据分类id查询商品:{}", categoryId);
        Good good = new Good();
        good.setCategoryId(categoryId);
        good.setStatus(StatusConstant.ENABLE);
        //获取商品列表
        List<GoodVO> GoodVOList = goodService.getByCategoryId(good);
        return Result.success(GoodVOList);
    }

    /**
     *  根据商品id查询商品详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation("根据商品id查询商品详情")
    public Result<GoodVO> getGoodDetail(Long id){
        log.info("根据商品id查询商品详情:{}", id);
        //获取商品详情
        GoodVO goodVO = goodService.getById(id);
        return Result.success(goodVO);
    }
}
