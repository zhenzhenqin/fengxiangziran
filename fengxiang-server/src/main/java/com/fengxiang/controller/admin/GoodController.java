package com.fengxiang.controller.admin;

import com.fengxiang.dto.GoodDTO;
import com.fengxiang.dto.GoodPageQueryDTO;
import com.fengxiang.entity.Good;
import com.fengxiang.result.PageResult;
import com.fengxiang.result.Result;
import com.fengxiang.service.GoodService;
import com.fengxiang.vo.GoodVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminGoodController")
@RequestMapping("/admin/dish")
@Api("商品管理")
@Slf4j
public class GoodController {


    @Autowired
    private GoodService goodService;

    /**
     * 分页查询商品
     * @param goodPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询商品")
    @GetMapping("/page")
    public Result<PageResult> page(GoodPageQueryDTO goodPageQueryDTO){
        log.info("分页查询商品参数:{}", goodPageQueryDTO);
        PageResult pageResult = goodService.pageQuery(goodPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增商品
     * @return
     */
    @ApiOperation("新增商品")
    @PostMapping
    public Result save(@RequestBody GoodDTO goodDTO){
        log.info("新增商品参数:{}", goodDTO);
        goodService.save(goodDTO);
        return Result.success();
    }

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @ApiOperation("根据id查询商品")
    @GetMapping("/{id}")
    public Result<GoodVO> getById(@PathVariable Long id){
        log.info("根据id查询商品参数:{}", id);
        GoodVO goodVO = goodService.getById(id);
        return Result.success(goodVO);
    }

    /**
     * 修改商品
     * @param goodDTO
     * @return
     */
    @ApiOperation("修改商品")
    @PutMapping
    public Result update(@RequestBody GoodDTO goodDTO){
        log.info("修改商品参数:{}", goodDTO);
        goodService.update(goodDTO);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result getByCategoryId(Long categoryId){
        log.info("根据分类id查询菜品:{}", categoryId);
        List<Good> list = goodService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 批量删除商品
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除商品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除商品:{}", ids);
        goodService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 批量起售或停售
     * @param status 起售或停售状态
     * @param id 商品id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("起售或停售商品")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("起售或停售商品参数:{}, {}", status, id);
        goodService.startOrStop(status, id);
        return Result.success();
    }
}
