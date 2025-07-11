package com.easyapi.excel.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/26 4:39 下午
 * @Version 1.0
 */
@Data
public class ExcelSheetVo implements Serializable {

    /**
     * 索引号
     */
    private Integer index;
    /**
     * sheet名
     */
    private String name;
    /**
     * 大标题
     */
    private String headerline;
    /**
     * 包含的列
     */
    private List<ExcelColumnVo> columnVos;

    /**
     * 额外标记字段
     */
    private String extId;
}
