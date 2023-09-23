package com.itheima.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie_take_out.pojo.Employee;

public interface EmpService extends IService<Employee> {
    Employee checklogin(Employee employee);

}
