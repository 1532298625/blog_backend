package com.graduation_project.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserVo {
    // 主键
    @JsonSerialize(using = ToStringSerializer.class)//解决long精度丢失问题
    private Long id;

    // 昵称
    private String nickName;
    //头像
    private String avatar;
    private String email;
}
