package com.fengxiang.mapper;

import com.fengxiang.annotation.AutoFill;
import com.fengxiang.dto.EmployeePageQueryDTO;
import com.fengxiang.entity.Employee;
import com.fengxiang.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 保存员工信息
     * @param employee
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into employee (username, name, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values (#{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void save(Employee employee);


    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    List<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}
