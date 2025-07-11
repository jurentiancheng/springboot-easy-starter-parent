package com.easyapi.jdbc.vo;

import lombok.Data;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/6 7:10 下午
 * @Version 1.0
 */
@Data
public class TableVo {

    /**
     * 表名称
     */
    private String name;

    /**
     * 表备注
     */
    private String common;
}
