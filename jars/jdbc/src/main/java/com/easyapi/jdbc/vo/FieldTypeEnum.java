package com.easyapi.jdbc.vo;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/8 1:42 下午
 * @Version 1.0
 */
public enum FieldTypeEnum {
    varchar("varchar", "字符型"),
    json("json", "JSON字段"),
    text("text", "Text 类型");

    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    FieldTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

}
