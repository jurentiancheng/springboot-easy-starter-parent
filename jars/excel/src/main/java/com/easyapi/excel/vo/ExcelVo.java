package com.easyapi.excel.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/8 4:11 下午
 * @Version 1.0
 */
@Data
public class ExcelVo implements Serializable {

    /**
     * excel 文件名
     */
    private String fileName;

    /**
     * sheet 名称
     */
    private String sheetName;

    /**
     * 大标题名称
     */
    private String headline;

    /**
     * 数据源字段-excel列明对应关系表
     */
    private List<FieldColumnRefVo> refVos;

    /**
     * 数据源
     */
    private JSONArray sourceData;

    @Data
    public static class FieldColumnRefVo {
        private String fieldName;
        private String columnText;
    }

    /**
     * excel 文件名称
     */
    private String name;

    /**
     * 包含的Sheets
     */
    private List<ExcelSheetVo> excelSheetVos;

    /**
     * 额外标记字段
     */
    private String extId;
}
