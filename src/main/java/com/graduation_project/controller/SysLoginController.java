package com.graduation_project.controller;


import com.graduation_project.dao.entity.LoginUser;
import com.graduation_project.dao.entity.User;
import com.graduation_project.service.impl.SysLoginService;
import com.graduation_project.utils.RedisCache;
import com.graduation_project.utils.VerificationCode;
import com.graduation_project.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2022-05-23 18:43:05
 */


@RequestMapping("user")
@Controller
@ResponseBody
public class SysLoginController {


    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private RedisTemplate redisTemplate;

    //生成验证码图片返回给前端图片地址
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("temp_code",text.toLowerCase());
        VerificationCode.output(image,resp.getOutputStream());
    }


    @PostMapping("/getcode")
    public ResponseResult getVerificationCode(@RequestBody User user) {
        return sysLoginService.getVerificationCode(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        return sysLoginService.register(user);
    }

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String code = valueOperations.get("temp_code").toString();
        if(user.getCode().toLowerCase().equals(code)){
            return sysLoginService.login(user);
        }else {
            return new ResponseResult(0,"验证码错误");
        }

    }

    @GetMapping("/logout")
    public ResponseResult logout() {
        return sysLoginService.logout();
    }

    @GetMapping("/info")
    public ResponseResult getInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseResult.success(loginUser.getUser());
    }
}

