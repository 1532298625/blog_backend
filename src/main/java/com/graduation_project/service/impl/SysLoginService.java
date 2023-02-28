package com.graduation_project.service.impl;

import com.graduation_project.dao.entity.LoginUser;
import com.graduation_project.dao.entity.User;
import com.graduation_project.utils.JwtUtil;
import com.graduation_project.vo.ResponseResult;
import com.graduation_project.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class SysLoginService{
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthenticationManager authenticationManager;
    public ResponseResult register(User user) {
        Boolean isRegister  = sysUserService.isRegister(user.getNickName(), user.getEmail());
        if (isRegister) {
            return ResponseResult.fail("用户名或邮箱已被注册");
        }
        Object code = redisTemplate.opsForValue().get("code:" + user.getEmail());
        if (Objects.nonNull(code)) {
            if (Objects.equals(code, user.getCode())) {
                //对密码做加密处理
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encode = passwordEncoder.encode(user.getPassword());
                user.setPassword(encode);
                sysUserService.save(user);
            }
            else {
                return ResponseResult.fail("验证码错误");
            }

        }
        else {
            return ResponseResult.fail("验证码已过期");
        }
        return  ResponseResult.success("注册成功");

    }

    public ResponseResult getVerificationCode(User user) {
        //生成验证码                                                     // 5 代表 位数
        String code = String.valueOf((int)((Math.random()*9+1)* Math.pow(10,5-1)));
                                                                //设置两分钟过期
        redisTemplate.opsForValue().set("code:"+user.getEmail(),code, Duration.ofMinutes(2));

        boolean b = emailService.sendSimpleMail(
                user.getEmail(),
                code
        );
        if (!b) {
            return ResponseResult.success("获取验证码失败，请检查邮箱是否正确");
        }
        return ResponseResult.success("验证码已发送 2分钟 内有效");
    }

    /**
     *   登录
     * @param user
     * @return
     */
    public ResponseResult login(User user) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //根据用户id生成 token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        redisTemplate.opsForValue().set("login:"+userId,loginUser,Duration.ofDays(30));

        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return  ResponseResult.success("登陆成功",map);

    }
    /**
     * 退出登录
     */
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 直接删除 redis就行
        Boolean delete = redisTemplate.delete("login:"+loginUser.getUser().getId());
        if (!delete) {
            return  ResponseResult.success("退出失败");
        }
        return  ResponseResult.success("退出成功");
    }

}
