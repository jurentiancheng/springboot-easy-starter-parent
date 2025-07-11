package com.easyapi.jdbc.service;

import com.alibaba.fastjson.JSONArray;
import com.easyapi.jdbc.SqlMaker;
import com.easyapi.jdbc.vo.FieldVo;
import com.easyapi.jdbc.vo.TableVo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/6 7:42 下午
 * @Version 1.0
 */
public interface SqlMakerService {

    /**
     * 执行sql查询
     * @param sql
     * @return
     */
    List<Map<String, Object>> query(String sql);

    /**
     * 执行sql 语句，非查询语句
     * @param sql
     * @return
     */
    Boolean excute(String sql);

    /**
     * 创建Create，Alter 表Sql语句
     * @param tableVo   表名对象
     * @param fieldVos  表字段对象
     * @param consumer  block 块
     */
    void makeSqlWith(TableVo tableVo, List<FieldVo> fieldVos, Consumer<SqlMaker> consumer);

    /**
     * 创建/更新表
     * @param tableVo   表名对象
     * @param fieldVos  表字段对象
     */
    void saveTableData(TableVo tableVo, List<FieldVo> fieldVos);

    /**
     * 创建/更新表，并根据提供的Json数据源插入数据
     * @param tableVo   表名对象
     * @param fieldVos  表字段对象
     * @param jsonArray json数据源
     */
    void saveTableData(TableVo tableVo, List<FieldVo> fieldVos, JSONArray jsonArray);

    /**
     * 创建/更新表后 回调方法
     * @param tableVo   表名对象
     * @param fieldVos  表字段对象
     * @param consumer  回调语句块
     */
    void saveTableData(TableVo tableVo, List<FieldVo> fieldVos, Consumer<SqlMaker> consumer);

}
