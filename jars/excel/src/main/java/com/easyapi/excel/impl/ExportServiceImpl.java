package com.easyapi.excel.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONObject;
import com.easyapi.excel.ExportService;
import com.easyapi.excel.vo.ExcelVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/8 4:36 下午
 * @Version 1.0
 */
@Service
@Slf4j
public class ExportServiceImpl implements ExportService {


    @Override
    public void exportFile(ExcelVo excelVo, Consumer<File> consumer) {
        Optional<String> optional = makeExcelFile(excelVo, null);
        optional.ifPresent(excelPath ->{
            File file = new File(excelPath);
            consumer.accept(file);
            // file.delete();
        });
    }

    @Override
    public void exportOutputStream(ExcelVo excelVo, Consumer<OutputStream> consumer) {
        Optional<String> optional = makeExcelFile(excelVo, null);
        optional.ifPresent(excelPath ->{
            try {
                File file = new File(excelPath);
                OutputStream outputStream = new FileOutputStream(file);
                consumer.accept(outputStream);
                file.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public void exportFile(ExcelVo excelVo, BiConsumer<ExcelWriter, WriteSheet> processConsumer, Consumer<File> consumer) {
        Optional<String> optional = makeExcelFile(excelVo, processConsumer);
        optional.ifPresent(excelPath ->{
            File file = new File(excelPath);
            consumer.accept(file);
            // file.delete();
        });
    }

    @Override
    public void exportOutputStream(ExcelVo excelVo, BiConsumer<ExcelWriter, WriteSheet> processConsumer, Consumer<OutputStream> consumer) {
        Optional<String> optional = makeExcelFile(excelVo, processConsumer);
        optional.ifPresent(excelPath ->{
            try {
                File file = new File(excelPath);
                OutputStream outputStream = new FileOutputStream(file);
                consumer.accept(outputStream);
                file.delete();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private Optional<String> makeExcelFile(ExcelVo excelVo, BiConsumer<ExcelWriter, WriteSheet> processConsumer) {
        log.info("Start makeExcelFile with fileName: {} ", excelVo.getFileName());
        String xlsPath = null;
        ExcelWriter excelWriter = null;
        File excelFile = null;
        try {
            //是否存在temp目录
            Path excelTmp = Paths.get("tmp_excel").toAbsolutePath();
            if (Files.notExists(excelTmp)) {
                Files.createDirectory(excelTmp);
            }
            Path resolve = excelTmp.resolve(excelVo.getFileName());
            if (Files.notExists(resolve)) {
                excelFile = new File(resolve.toUri());
            } else {
                excelFile = resolve.toFile();
            }
            log.info("Start makeExcelFile maked excelFile: {} write to sheetName:{}", excelFile, excelVo.getSheetName());
            excelWriter = EasyExcel.write(excelFile).build();
            WriteSheet defaultSheet = EasyExcel.writerSheet(0, excelVo.getSheetName()).head(this.makeHead(excelVo)).build();
            // List<Map> list = JSONObject.parseArray(excelVo.getSourceData().toJSONString(),Map.class);
            List<ExcelVo.FieldColumnRefVo> fieldColumnRefVo = excelVo.getRefVos();
            if (processConsumer != null) {
                processConsumer.accept(excelWriter, defaultSheet);
            } else {
                List<List<Object>> excelData =
                        excelVo.getSourceData().stream()
                                .map(object -> {
                                    JSONObject objectJson = (JSONObject) object;
                                    JSONObject newObj = new JSONObject(true);
                                    fieldColumnRefVo.stream().forEach(fieldColumnRefVo1 -> {
                                        newObj.putIfAbsent(fieldColumnRefVo1.getFieldName(), objectJson.get(fieldColumnRefVo1.getFieldName()));
                                    });
                                    return newObj.values().stream().collect(Collectors.toList());
                                })
                                .collect(Collectors.toList());
                excelWriter.write(excelData, defaultSheet);
            }
            xlsPath = excelFile.getCanonicalPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        log.info("End makeExcelFile maked relative Path : {}", xlsPath);
        return Optional.ofNullable(xlsPath);
    }

    private List<List<String>> makeHead(ExcelVo excelVo) {
        List<List<String>> heads = excelVo.getRefVos().stream()
                .map(fieldColumnRefVo -> {
                    List<String> head = new ArrayList<>();
                    head.add(fieldColumnRefVo.getColumnText());
                    return head;
                }).collect(Collectors.toList());
        return heads;
    }

}
