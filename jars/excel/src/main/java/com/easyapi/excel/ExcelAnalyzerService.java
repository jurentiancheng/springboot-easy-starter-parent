package com.easyapi.excel;

import com.alibaba.excel.context.AnalysisContext;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/26 4:33 下午
 * @Version 1.0
 */
public interface ExcelAnalyzerService {

    /**
     * 解析Excel 获取所有sheet中定义的列标题
     * @param file
     * @return
     */
    //ExcelVo filterHeaderTitles(File file, Integer headerRowIndex);

    /**
     * 解析excel 文件
     * @param file
     * @param headerConsumer
     * @param contentConsumer
     */
    void analyze(File file,
                 BiConsumer<Map<Integer, String>, AnalysisContext> headerConsumer,
                 BiConsumer<List<Map<Integer, String>>, AnalysisContext> contentConsumer);


}
