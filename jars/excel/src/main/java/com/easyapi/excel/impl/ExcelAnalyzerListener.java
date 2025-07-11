package com.easyapi.excel.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/27 3:58 下午
 * @Version 1.0
 */
public class ExcelAnalyzerListener extends AnalysisEventListener<Map<Integer, String>> {

    private static final Logger log = LoggerFactory.getLogger(ExcelAnalyzerListener.class);

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private int BATCH_COUNT = 2000;
    List<Map<Integer, String>> list = new ArrayList<>();
    private final BiConsumer<Map<Integer, String>, AnalysisContext> headerConsumer;
    private final BiConsumer<List<Map<Integer, String>>, AnalysisContext> contentConsumer;

    public ExcelAnalyzerListener(BiConsumer<Map<Integer, String>, AnalysisContext> headerConsumer,
                                 BiConsumer<List<Map<Integer, String>>, AnalysisContext> contentConsumer) {
        this.headerConsumer = headerConsumer;
        this.contentConsumer = contentConsumer;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析一条表头{}", context.readSheetHolder().getSheetName());
        headerConsumer.accept(headMap, context);
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", data.getOrDefault(0, "ok")); //JSON.toJSONString(data)
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            contentConsumer.accept(list, analysisContext);
            // 存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        contentConsumer.accept(list, analysisContext);
        // 多个sheet 时也需要清理
        list.clear();
    }

    /**
     * 设置export size
     * @param batchSize
     */
    public void setExportBatchSize(Integer batchSize) {
        this.BATCH_COUNT = batchSize;
    }
}
