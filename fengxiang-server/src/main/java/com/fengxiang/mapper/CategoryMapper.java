package com.fengxiang.mapper;

import com.fengxiang.annotation.AutoFill;
import com.fengxiang.dto.CategoryPageQueryDTO;
import com.fengxiang.entity.Category;
import com.fengxiang.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category (name, sort, status, create_time, update_time, create_user, update_user) " +
            "values (#{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 分页查询分类
     * @param categoryPageQuery
     * @return
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQuery);

    /**
     * 修改分类
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 删除分类
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Long id);

    /**
     * 根据id查询分类
     * @param id
     * @return
     */
    @Select("select * from category where id = #{id}")
    Category getById(Long id);

    /**
     * 查询所有分类
     * @return
     */
    @Select("select * from category where status = #{status} order by sort")
    List<Category> getCategoryList(Category category);
}
