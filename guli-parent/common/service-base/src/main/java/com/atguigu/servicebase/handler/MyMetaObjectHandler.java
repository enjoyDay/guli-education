package com.atguigu.servicebase.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author liukun
 * @description 数据库增改数据时，mp数据填充类
 * @date 2020/6/6
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入到数据库的时候
     *
     * @param metaObject 元数据
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate", new Date(), metaObject);
        this.setFieldValByName("gmtModified", new Date(), metaObject);
        this.setFieldValByName("isDeleted", false, metaObject);
        this.setFieldValByName("version",1L,metaObject);
    }

    /**
     * 更新数据的时候
     *
     * @param metaObject 元数据
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified", new Date(), metaObject);
    }
}
