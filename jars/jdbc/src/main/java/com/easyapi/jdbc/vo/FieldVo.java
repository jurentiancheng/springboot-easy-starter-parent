package com.easyapi.jdbc.vo;

import lombok.Data;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/6 7:11 下午
 * @Version 1.0
 */
@Data
public class FieldVo {

    /**
     * 字段名
     */
    private String name;

    /**
     * 字段注释
     */
    private String comment;

    /**
     * 字段值类型
     */
    private FieldTypeEnum valueType;

    /**
     * 长度
     */
    private Integer length = 125;

    /**
     * 源数据唯一映射字段
     */
    private String sourceKeyRef;


}
