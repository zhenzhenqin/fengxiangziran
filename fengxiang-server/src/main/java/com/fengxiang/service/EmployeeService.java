package com.fengxiang.service;

import com.fengxiang.dto.EmployeeLoginDTO;
import com.fengxiang.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
