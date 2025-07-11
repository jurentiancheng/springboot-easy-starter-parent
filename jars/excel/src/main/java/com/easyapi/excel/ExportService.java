package com.easyapi.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.easyapi.excel.vo.ExcelVo;

import java.io.File;
import java.io.OutputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/8 4:02 下午
 * @Version 1.0
 */
public interface ExportService {

    void exportFile(ExcelVo excelVo, Consumer<File> consumer);

    void exportFile(ExcelVo excelVo, BiConsumer<ExcelWriter, WriteSheet>processConsumer, Consumer<File> consumer);

    void exportOutputStream(ExcelVo excelVo, Consumer<OutputStream> consumer);

    void exportOutputStream(ExcelVo excelVo, BiConsumer<ExcelWriter, WriteSheet>processConsumer, Consumer<OutputStream> consumer);

}
