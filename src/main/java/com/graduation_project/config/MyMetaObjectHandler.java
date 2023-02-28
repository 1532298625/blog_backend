package com.graduation_project.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component //一定不要忘记把处理器加到IOC容器中
public class MyMetaObjectHandler  implements MetaObjectHandler {
    //插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        //第一个参数 字段名 
        //第二个参数 值
        //第三个参数 metaObject
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("createDate",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("userType","1",metaObject);
        this.setFieldValByName("status","0",metaObject);
        this.setFieldValByName("sex","2",metaObject);
        this.setFieldValByName("delFlag",0,metaObject);
        this.setFieldValByName("weight",0,metaObject);
        this.setFieldValByName("viewCounts",0,metaObject);
        this.setFieldValByName("commentCounts",0,metaObject);
        this.setFieldValByName("likenum",0,metaObject);
    }
	//更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
