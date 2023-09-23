package com.itheima.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie_take_out.pojo.User;
import com.itheima.reggie_take_out.service.UserService;
import com.itheima.reggie_take_out.utils.R;
import com.itheima.reggie_take_out.utils.SMSUtils;
import com.itheima.reggie_take_out.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //判断手机号是否已经注册
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("生成的验证码为："+code);
            //调用阿里云短信服务发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //将验证码存入session
            session.setAttribute(phone,code);
            return R.success("发送成功");
        }

        return R.error("发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = (String) map.get("phone");
        //获取验证码
        String code = (String) map.get("code");
        //判断验证码是否正确
        String sessionCode = (String) session.getAttribute(phone);
        if(sessionCode==null || !code.equals(sessionCode)){
            return R.error("验证码错误");
        }
        //判断当前用户是否为新用户，如果为新用户就自动完成注册
        LambdaQueryWrapper<User> lw = new LambdaQueryWrapper<>();
        lw.eq(User::getPhone,phone);
        User user = userService.getOne(lw);
        if(user == null){
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user",user.getId());
        return R.success(user);
    }
}
