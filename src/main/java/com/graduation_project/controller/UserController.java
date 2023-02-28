package com.graduation_project.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.graduation_project.dao.entity.LoginUser;
import com.graduation_project.dao.entity.User;
import com.graduation_project.service.SysUserService;
import com.graduation_project.utils.JwtUtil;
import com.graduation_project.vo.ResponseResult;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Objects;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private SysUserService userService;

    @PostMapping("/resetPassword")
    public ResponseResult resetPassword(@RequestBody String user){
        User user_json = JSON.parseObject(user, User.class);
        String userName = user_json.getUserName();
        String password = user_json.getPassword();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_name",userName);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        updateWrapper.set("password", bCryptPasswordEncoder.encode(password));
        boolean update = userService.update(updateWrapper);
        if (update){
            return new ResponseResult(200,"密码重置成功");
        }
        return new ResponseResult(300,"密码重置失败");
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAuthority('system:user:changepassword')")
    public ResponseResult changePassword(@RequestBody String user) throws Exception {
        User user_json = JSON.parseObject(user, User.class);
        String password = user_json.getPassword();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);
        String userid = claims.getSubject();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", userid);
        updateWrapper.set("password", bCryptPasswordEncoder.encode(password));
        boolean update = userService.update(updateWrapper);
        if (update){
            return new ResponseResult(200,"密码修改成功");
        }
        return new ResponseResult(300,"密码修改失败");
    }

    @PostMapping("/getInfo")
    @PreAuthorize("hasAuthority('system:user:getinfo')")
    public ResponseResult getInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long userid = loginUser.getUser().getId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userid);
        User user = userService.getOne(queryWrapper);
        if (!Objects.isNull(user)){
            user.setPassword("**********");
            return new ResponseResult(200,"获取用户信息成功",user);
        }else {
            return new ResponseResult(300,"获取用户信息失败");
        }
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/changeInfo")
    @PreAuthorize("hasAuthority('system:user:changeinfo')")
    public ResponseResult changeInfo(@RequestBody String user) throws Exception {
        User user_json = JSON.parseObject(user, User.class);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long userid = loginUser.getUser().getId();
        updateWrapper.eq("id", userid);
        if (!Objects.isNull(user_json.getNickName())){
            updateWrapper.set("nick_name", user_json.getNickName());
            loginUser.getUser().setNickName(user_json.getNickName());
        }
        if (!Objects.isNull(user_json.getAvatar())){
            updateWrapper.set("avatar", user_json.getAvatar());
            loginUser.getUser().setAvatar(user_json.getAvatar());
        }
        if (!Objects.isNull(user_json.getEmail())){
            updateWrapper.set("email", user_json.getEmail());
            loginUser.getUser().setEmail(user_json.getEmail());
        }
        if (!Objects.isNull(user_json.getPhonenumber())){
            updateWrapper.set("phonenumber", user_json.getPhonenumber());
            loginUser.getUser().setPhonenumber(user_json.getPhonenumber());
        }
        if (!Objects.isNull(user_json.getSex())){
            updateWrapper.set("sex", user_json.getSex());
            loginUser.getUser().setSex(user_json.getSex());
        }
        redisTemplate.opsForValue().set("login:"+userid,loginUser, Duration.ofDays(30));
        boolean update = userService.update(updateWrapper);
        if (update){
            return new ResponseResult(200,"用户信息修改成功");
        }else {
            return new ResponseResult(300,"用户信息修改失败");
        }
    }
}
