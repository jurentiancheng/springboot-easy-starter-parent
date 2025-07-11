package com.easyapi.jdbc.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easyapi.jdbc.SqlMaker;
import com.easyapi.jdbc.config.DataSourceConfiguration;
import com.easyapi.jdbc.vo.FieldVo;
import com.easyapi.jdbc.vo.TableVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/6 7:23 下午
 * @Version 1.0
 */
@Slf4j
public class SqlMakerServiceImpl implements SqlMakerService {

    @Resource(name = "jdbcTemplateDefault")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("jdbcTemplateSystem")
    private JdbcTemplate jdbcSystemTemplate;

    @Autowired
    private DataSourceConfiguration dataSourceConfiguration;


    /**
     * @param sql
     * @return
     */
    @Override
    public List<Map<String, Object>> query(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 创建Create，Alter 表Sql语句
     * @param tableVo   表名对象
     * @param fieldVos  表字段对象
     * @param consumer  block 块
     */
    @Override
    public void makeSqlWith(TableVo tableVo, List<FieldVo> fieldVos, Consumer<SqlMaker> consumer) {
        SqlMaker sqlMaker = new SqlMaker.Builder(tableVo, fieldVos).build();
        if (consumer != null) {
            consumer.accept(sqlMaker);
        }
    }

    /**
     * 创建/更新表
     * @param tableVo   表名对象
     * @param fieldVos  表字段对象
     */
    @Override
    public void saveTableData(TableVo tableVo, List<FieldVo> fieldVos) {
        SqlMaker.Builder builder =  new SqlMaker.Builder(tableVo, fieldVos);
        SqlMaker sqlMaker = builder.setConfiguration(dataSourceConfiguration)
                .build();
        // 检查表是否存在，确定是create操作还是alter 操作
        try {
            String checkTableSql = sqlMaker.makeCheckTableSql();
            Integer nums = jdbcSystemTemplate.queryForObject(checkTableSql, Integer.class);
            if (nums > 0) {
                // 暂不处理修改的场景
                // this.alterTable(tableVo,fieldVos);
            } else {
                this.createTable(tableVo,fieldVos);
            }
        }catch (Exception e){
            log.info("saveTableData error:{}", e.getMessage());
        }
    }

    /**
     * 创建/更新表，并根据提供的Json数据源插入数据
     * @param tableVo   表名对象
     * @param fieldVos  表字段对象
     * @param jsonArray json数据源
     */
    @Override
    public void saveTableData(TableVo tableVo, List<FieldVo> fieldVos, JSONArray jsonArray) {
        SqlMaker sqlMaker = new SqlMaker.Builder(tableVo, fieldVos).setConfiguration(dataSourceConfiguration).build();
        this.saveTableData(tableVo, fieldVos);
        if (jsonArray != null) {
            // 根据filedVos 中key 顺序读取 jsonFile 对应的值
            String valuesStr =  jsonArray.stream().map(jsonObj -> {
                JSONObject jsonItem = (JSONObject) jsonObj;
                StringBuilder builder = new StringBuilder();
                builder.append("(");
                fieldVos.forEach(
                    fieldVo -> {
                        String fieldValue = jsonItem.getString(fieldVo.getSourceKeyRef().replace("`",""));
                        fieldValue = (fieldValue == null) ? "" : fieldValue;
                        builder.append("'").append(fieldValue).append("',");
                    }
                );
                builder.replace(builder.length() - 1 ,builder.length(), " ");
                builder.append(")");
                return builder.toString();
            }).collect(Collectors.joining(","));
            Optional<String> batchInsertSql = sqlMaker.makeInsertSql(() -> valuesStr);
            batchInsertSql.ifPresent(this::excute);
        }
    }

    /**
     * 动态生成表的创建及更新表结构，回调执行结果
     *
     * @param tableVo
     * @param fieldVos
     * @param consumer
     */
    @Override
    public void saveTableData(TableVo tableVo, List<FieldVo> fieldVos, Consumer<SqlMaker> consumer) {
        SqlMaker.Builder builder =  new SqlMaker.Builder(tableVo, fieldVos);
        SqlMaker sqlMaker = builder.setConfiguration(dataSourceConfiguration).build();
        this.saveTableData(tableVo, fieldVos);
        if ( consumer!= null) {
            consumer.accept(sqlMaker);
        }
    }

    /**
     * 执行sql 语句，非查询语句
     * @param sql
     * @return
     */
    @Override
    public Boolean excute(String sql) {
        log.info("start excute sql:{}", sql);
        try {
            this.jdbcTemplate.execute(sql);
            return true;
        }catch (Exception e){
            log.error("excuteSql error:{}", e.getMessage());
        }
        return false;
    }

    private Boolean createTable(TableVo tableVo, List<FieldVo> fieldVos) {
        SqlMaker sqlMaker = new SqlMaker.Builder(tableVo, fieldVos).setConfiguration(dataSourceConfiguration).build();
        Optional<String> createSqlOpt = sqlMaker.makeCreateSql(() -> {
            // 校验数据之类的前置操作
            // TODO...
            return true;
        });
        return createSqlOpt
                .map(this::excute)
                .orElse(false);
    }

    private Boolean alterTable(TableVo tableVo, List<FieldVo> fieldVos) {
        SqlMaker sqlMaker = new SqlMaker.Builder(tableVo, fieldVos).setConfiguration(dataSourceConfiguration).build();
        Optional<String> alterSqlOpt = sqlMaker.makeAlterSql(tableColumnSearchSql -> jdbcSystemTemplate.queryForList(tableColumnSearchSql));
        return alterSqlOpt
                .map(this::excute)
                .orElse(false);
    }

}
