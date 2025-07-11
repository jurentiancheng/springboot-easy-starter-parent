package com.easyapi.excel.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.easyapi.excel.ExcelAnalyzerService;
import com.easyapi.excel.config.AnalyzerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/26 4:34 下午
 * @Version 1.0
 */
@Slf4j
@Service
public class ExcelAnalyzerServiceImpl implements ExcelAnalyzerService {

    @Autowired
    private AnalyzerConfig analyzerConfig;

    @Override
    public void analyze(File file,
                        BiConsumer<Map<Integer, String>, AnalysisContext> headerConsumer,
                        BiConsumer<List<Map<Integer, String>>, AnalysisContext> contentConsumer) {
        ExcelReader excelReader = null;
        try {
            ExcelAnalyzerListener excelAnalyzerListener = new ExcelAnalyzerListener(
                    (headerMap, analyzeContext) -> {
                        headerConsumer.accept(headerMap, analyzeContext);
                    },
                    (list, analyzeContext) -> {
                        // 接收Excel sheetHeader 解析的回调事件
                        contentConsumer.accept(list, analyzeContext);
                    }
            );
            excelAnalyzerListener.setExportBatchSize(analyzerConfig.getBatchSize());
            excelReader = EasyExcel.read(file, excelAnalyzerListener).headRowNumber(1).build();
            excelReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }

}
