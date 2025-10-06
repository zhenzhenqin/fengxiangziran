package com.fengxiang.controller.admin;

import com.fengxiang.dto.CategoryDTO;
import com.fengxiang.dto.CategoryPageQueryDTO;
import com.fengxiang.result.PageResult;
import com.fengxiang.result.Result;
import com.fengxiang.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @return
     */
    @ApiOperation("新增分类")
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类");
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询分类
     * @param categoryPageQuery
     * @return
     */
    @ApiOperation("分页查询分类")
    @GetMapping("/page")
    public Result page(CategoryPageQueryDTO categoryPageQuery){
        log.info("分页查询分类:{}",categoryPageQuery);
        PageResult pageResult = categoryService.pageQuery(categoryPageQuery);
        return Result.success(pageResult);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("修改分类")
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类:{}",categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 启用 禁用分类
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用 禁用分类")
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("修改分类状态:{},{}",status,id);
        categoryService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @ApiOperation("删除分类")
    @DeleteMapping
    public Result delete(Long id){
        log.info("删除分类:{}",id);
        categoryService.delete(id);
        return Result.success();
    }
}