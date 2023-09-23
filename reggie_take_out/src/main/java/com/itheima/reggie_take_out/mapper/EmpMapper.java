package com.itheima.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie_take_out.pojo.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface EmpMapper extends BaseMapper<Employee> {
    @Select("select * from employee where username=#{username} and password=#{password}")
    public Employee checklogin(Employee employee);


}
