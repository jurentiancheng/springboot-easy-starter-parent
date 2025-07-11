package com.easyapi.excel.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/26 4:39 下午
 * @Version 1.0
 */
@Data
public class ExcelColumnVo implements Serializable {

    /**
     * 列序号
     */
    private Integer index;

    /**
     * 列名称
     */
    private String name;

    /**
     * 值类型
     */
    private ColumnValueTypeEnum valueType;

    /**
     * 额外标记字段
     */
    private String extId;
}
