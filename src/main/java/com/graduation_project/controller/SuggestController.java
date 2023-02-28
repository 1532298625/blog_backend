package com.graduation_project.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.graduation_project.dao.entity.CommonPage;
import com.graduation_project.dao.entity.LoginUser;
import com.graduation_project.dao.entity.Suggest;
import com.graduation_project.service.SuggestService;
import com.graduation_project.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("suggest")
public class SuggestController {

    @Autowired
    private SuggestService suggestService;

    @PostMapping("/addSuggest")
    @PreAuthorize("hasAuthority('system:common:addSuggest')")
    private ResponseResult AddSuggest(@RequestBody Suggest suggest){
        //获取当前用户 id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        suggest.setUserid(loginUser.getUser().getId());
        suggestService.save(suggest);
        return new ResponseResult(200,"保存建议成功");
    }

    @PostMapping("/listSuggest")
    @PreAuthorize("hasAuthority('system:admin:listSuggest')")
    private ResponseResult ListSuggest(@RequestParam(required = false) String username,
                                       @RequestParam(required = false) String title,
                                       @RequestParam(required = false) String content,
                                       @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        Page<Suggest> allSuggest = suggestService.list(username,title,content,pageSize,pageNum);
        return new ResponseResult(200,"获取分页建议成功", CommonPage.restPage(allSuggest));
    }

    @PostMapping("/deleteSuggest")
    @PreAuthorize("hasAuthority('system:admin:deleteSuggest')")
    private ResponseResult DeleteSuggest(@RequestBody Suggest suggest){
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",suggest.getId());
        updateWrapper.set("del_flag",1);
        boolean update = suggestService.update(updateWrapper);
        if (update){
            return new ResponseResult(200,"删除建议成功");
        }else {
            return new ResponseResult(300,"删除建议失败");
        }
    }

}
