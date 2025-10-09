package com.fengxiang.controller.user;

import com.fengxiang.entity.Category;
import com.fengxiang.result.Result;
import com.fengxiang.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "C端-分类接口")
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类
     * @return
     */
    @ApiOperation("查询所有分类")
    @GetMapping("/list")
    public Result<List<Category>> list() {
        log.info("查询所有分类参数");
        List<Category> list = categoryService.getCategoryList();
        return Result.success(list);
    }
}
