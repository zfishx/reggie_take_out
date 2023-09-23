package com.itheima.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie_take_out.pojo.Employee;
import com.itheima.reggie_take_out.service.EmpService;
import com.itheima.reggie_take_out.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmpController {
    @Autowired
    private EmpService empservice;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest req, @RequestBody Employee employee){
        System.out.println("员工登录");
        String p = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        employee.setPassword(p);
        Employee e = empservice.checklogin(employee);
        if(e==null){
            return R.error("用户名或密码错误");
        }
        if(e.getStatus()==0){
            return R.error("账号已被禁用");
        }
        req.getSession().setAttribute("employee",e.getId());//将员工id存入session
        return R.success(e);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest req){
        System.out.println("员工登出");
        req.getSession().removeAttribute("employee");
        return R.success("登出成功");
    }

    @PostMapping
    public R<String> add(HttpServletRequest req,@RequestBody Employee emp){
        System.out.println("添加员工");
        String p = DigestUtils.md5DigestAsHex("123456".getBytes());
        emp.setPassword(p);
        /*emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        //获得当前登录的管理员id
        Long adminid = (Long) req.getSession().getAttribute("employee");
        emp.setCreateUser(adminid);
        emp.setUpdateUser(adminid);*/
        //System.out.println("admin:"+req.getSession().getAttribute("employee"));
       /* try{
            empservice.save(emp);
        }catch (Exception e){
            System.out.println("新增员工失败"+e);
            return R.error("新增员工失败");
        }*/
        empservice.save(emp);
        return R.success("新增员工成功");
    }
    //员工信息分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page pageParam = new Page(page,pageSize);
        //构造查询条件构造器
        LambdaQueryWrapper<Employee> qw = new LambdaQueryWrapper<Employee>();
        qw.like(name!=null,Employee::getName,name);
        qw.orderByDesc(Employee::getUpdateTime);
        empservice.page(pageParam,qw);
        return R.success(pageParam);
    }

    //员工信息修改
    @PutMapping
    public R<String> update(HttpServletRequest req,@RequestBody Employee emp){
        System.out.println("修改员工信息");
        //获得当前登录的管理员id
        /*Long adminid = (Long) req.getSession().getAttribute("employee");
        emp.setUpdateUser(adminid);
        emp.setUpdateTime(LocalDateTime.now());*/
        empservice.updateById(emp);
        return R.success("修改员工信息成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getbyid(@PathVariable  Long id){
        System.out.println("根据id查询员工信息");
        System.out.println(id);
        Employee emp = empservice.getById(id);
        if(emp != null) return R.success(emp);
        else return R.error("未找到该员工");
    }
}
