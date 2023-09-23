package com.itheima.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie_take_out.mapper.EmpMapper;
import com.itheima.reggie_take_out.pojo.Employee;
import com.itheima.reggie_take_out.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper,Employee> implements EmpService {
    @Autowired
    private EmpMapper empmapper;
    @Override
    public Employee checklogin(Employee employee) {
        System.out.println(employee.getPassword());
        return empmapper.checklogin(employee);
    }

}
