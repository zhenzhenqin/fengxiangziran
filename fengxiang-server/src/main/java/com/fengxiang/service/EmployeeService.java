package com.fengxiang.service;

import com.fengxiang.dto.EmployeeDTO;
import com.fengxiang.dto.EmployeeLoginDTO;
import com.fengxiang.dto.EmployeePageQueryDTO;
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
}
