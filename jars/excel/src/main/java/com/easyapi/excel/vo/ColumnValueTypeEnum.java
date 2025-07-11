package com.easyapi.excel.vo;


import lombok.Getter;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/8 1:42 下午
 * @Version 1.0
 */
@Getter
public enum ColumnValueTypeEnum {
    varchar("varchar", "字符型"),
    json("json", "JSON字段");

    private final String code;

    private final String name;

    ColumnValueTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

}
