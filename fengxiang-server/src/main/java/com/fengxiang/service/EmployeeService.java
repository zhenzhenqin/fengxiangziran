package com.fengxiang.service;

import com.fengxiang.dto.EmployeeDTO;
import com.fengxiang.dto.EmployeeLoginDTO;
import com.fengxiang.dto.EmployeePageQueryDTO;
import com.fengxiang.dto.PasswordEditDTO;
import com.fengxiang.entity.Employee;
import com.fengxiang.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);


    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee getById(Integer id);

    /**
     * 编辑员工密码
     * @param passwordEditDTO
     */
    void update(PasswordEditDTO passwordEditDTO);


    /**
     * 修改员工信息
     * @param employeeDTO
     */
    void updateEmployee(EmployeeDTO employeeDTO);
}
