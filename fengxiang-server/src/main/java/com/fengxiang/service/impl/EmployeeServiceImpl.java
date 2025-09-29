package com.fengxiang.service.impl;

import com.fengxiang.constant.MessageConstant;
import com.fengxiang.constant.PasswordConstant;
import com.fengxiang.constant.StatusConstant;
import com.fengxiang.context.BaseContext;
import com.fengxiang.dto.EmployeeDTO;
import com.fengxiang.dto.EmployeeLoginDTO;
import com.fengxiang.dto.EmployeePageQueryDTO;
import com.fengxiang.entity.Employee;
import com.fengxiang.exception.AccountLockedException;
import com.fengxiang.exception.AccountNotFoundException;
import com.fengxiang.exception.PasswordErrorException;
import com.fengxiang.mapper.EmployeeMapper;
import com.fengxiang.result.PageResult;
import com.fengxiang.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置状态为启用
        employee.setStatus(StatusConstant.DISABLE);  //设置默认状态为禁用

        //设置初始密码 进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));  //设置默认初始密码为123456

        //存入数据库
        employeeMapper.save(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //分页配置
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        List<Employee> list = employeeMapper.pageQuery(employeePageQueryDTO);

        Page<Employee> page = (Page<Employee>) list;

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 获取当前登录员工ID（从JWT或上下文中获取）
        Long currentEmpId = BaseContext.getCurrentId();

        // 判断是否为管理员（如admin账户）
        Employee currentEmployee = employeeMapper.getById(currentEmpId);

        // 如果不是管理员，则只能操作自己的账户
        if (!"admin".equals(currentEmployee.getUsername()) && !currentEmpId.equals(id)) {
            throw new RuntimeException("权限不足，只能操作自己的账户");
        }
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();

        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        return employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        // 获取当前登录员工ID（从JWT或上下文中获取）
        Long currentEmpId = BaseContext.getCurrentId();

        // 判断是否为管理员（如admin账户）
        Employee currentEmployee = employeeMapper.getById(currentEmpId);

        // 如果不是管理员，则只能操作自己的账户
        if (!"admin".equals(currentEmployee.getUsername()) && !currentEmpId.equals(employee.getId())) {
            throw new RuntimeException("权限不足，只能操作自己的账户");
        }

        employeeMapper.update(employee);
    }

}
