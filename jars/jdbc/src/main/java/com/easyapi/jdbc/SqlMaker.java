package com.easyapi.jdbc;

import com.easyapi.jdbc.config.DataSourceConfiguration;
import com.easyapi.jdbc.vo.FieldTypeEnum;
import com.easyapi.jdbc.vo.FieldVo;
import com.easyapi.jdbc.vo.TableVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/7 11:00 上午
 * @Version 1.0
 */
@Slf4j
public class SqlMaker {

    private TableVo tableVo;

    private List<FieldVo> fieldVos;

    private DataSourceConfiguration configuration;

    private SqlMaker(){

    }

    private SqlMaker(Builder builder) {
        this.tableVo = builder.tableVo;
        this.fieldVos = builder.fieldVos;
        this.configuration = builder.configuration;
    }

    public String makeCheckTableSql() {
        String sql = "SELECT COUNT(*)nums FROM TABLES WHERE TABLE_SCHEMA = '"+ configuration.getSchema() +"' AND TABLE_NAME='"+ tableVo.getName() +"'";
        return sql;
    }

    public Optional<String> makeQuerySql() {
        String fields = fieldVos.stream()
                .map(fieldVo -> {
                    if (!fieldVo.getName().startsWith("`")) {
                        fieldVo.setName("`".concat(fieldVo.getName()).concat("`"));
                    }
                    return fieldVo.getName();
                })
                .collect(Collectors.joining(","));
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ")
                .append(fields)
                .append(" FROM ")
                .append(tableVo.getName());
        return Optional.ofNullable(builder.toString());
    }

    public Optional<String> makeCreateSql(Supplier<Boolean> supplier) {
        if (supplier != null) {
           Boolean ret = supplier.get();
           if (!ret) {
               return Optional.empty();
           }
        }
        if (CollectionUtils.isEmpty(fieldVos)) {
            return Optional.empty();
        }
        // 遍历生成create 语句
        fieldVos.stream().forEach(fieldVo -> {
            if (!fieldVo.getName().startsWith("`")) {
                fieldVo.setName("`".concat(fieldVo.getName()).concat("`"));
            }
        });
        StringBuilder builder = new StringBuilder();
        builder = builder.append("CREATE TABLE ").append("`").append(tableVo.getName()).append("`").append("(");
        // 拼接ID主键,状态
        builder = builder.append("`").append("id").append("`").append("int (11) NOT NULL AUTO_INCREMENT,");
        builder = builder.append("`").append("status").append("`").append("tinyint (4) NOT NULL DEFAULT 0,");
        StringBuilder finalBuilder = builder;
        fieldVos.stream().forEach(fieldVo -> {
            if (ObjectUtils.isEmpty(fieldVo.getValueType()) || FieldTypeEnum.varchar.equals(fieldVo.getValueType())) {
                finalBuilder.append(fieldVo.getName()).append(" varchar(").append(fieldVo.getLength()).append(")").append(" DEFAULT NULL COMMENT").append("'").append(ObjectUtils.isEmpty(fieldVo.getComment()) ? "" : fieldVo.getComment()).append("'").append(",");
            } else {
                finalBuilder.append(fieldVo.getName()).append(fieldVo.getValueType().getCode()).append(" DEFAULT NULL COMMENT").append("'").append(ObjectUtils.isEmpty(fieldVo.getComment()) ? "" : fieldVo.getComment()).append("'").append(",");
            }
        });
        builder = finalBuilder.append("PRIMARY KEY (`id`) USING BTREE)");
        builder = builder
                .append("ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT")
                .append("'").append(tableVo.getCommon()).append("';");
        return Optional.ofNullable(builder.toString());
    }

    public Optional<String> makeAlterSql(ISql<Map<String, Object>> function) {
        try {
            // 匹配字段生成 Edit 语句
            String columnSearchSql = "SELECT COLUMN_NAME FROM COLUMNS WHERE TABLE_SCHEMA = '"+ configuration.getSchema() +"' AND TABLE_NAME = '"+ tableVo.getName() +"';";
            List<Map<String, Object>> columns = function.accept(columnSearchSql);
            // 过滤出需要新增的字段
            fieldVos = fieldVos.stream().filter(fieldVo ->
                    (columns.stream().filter(item -> {
                        if (item.get("COLUMN_NAME").equals(fieldVo.getName())) {
                            return true;
                        }
                        return false;
                    }).count() > 0) ? false : true
            ).collect(Collectors.toList());

            // 遍历生成Edit 语句
            if (!CollectionUtils.isEmpty(fieldVos)) {
                fieldVos.stream().forEach(fieldVo -> {
                    if (!fieldVo.getName().startsWith("`")) {
                        fieldVo.setName("`".concat(fieldVo.getName()).concat("`"));
                    }
                });
                StringBuilder builder = new StringBuilder();
                builder = builder.append("ALTER TABLE ").append("`").append(tableVo.getName()).append("` ");
                StringBuilder finalBuilder = builder;
                fieldVos.stream().forEach(fieldVo -> {
                    if (ObjectUtils.isEmpty(fieldVo.getValueType()) || FieldTypeEnum.varchar.equals(fieldVo.getValueType())) {
                        finalBuilder.append("ADD COLUMN ").append(fieldVo.getName()).append(" varchar(").append(fieldVo.getLength()).append(")").append(" DEFAULT NULL COMMENT").append("'").append(ObjectUtils.isEmpty(fieldVo.getComment()) ? "" : fieldVo.getComment()).append("'").append(",");
                    } else {
                        finalBuilder.append("ADD COLUMN ").append(fieldVo.getName()).append(" ").append(fieldVo.getValueType().getCode()).append(" DEFAULT NULL COMMENT").append("'").append(ObjectUtils.isEmpty(fieldVo.getComment()) ? "" : fieldVo.getComment()).append("'").append(",");
                    }
                });
                builder = finalBuilder.replace(finalBuilder.length() - 1, finalBuilder.length(), ";");
               return Optional.ofNullable(builder.toString());
            }
        }catch (Exception e){
            log.info("makeEditTableSql error:{}", e.getMessage());
        }
        return Optional.empty();
    }


    public Optional<String> makeInsertSql(Supplier<String> supplier) {
        // 遍历生成create 语句
        fieldVos.stream().forEach(fieldVo -> {
            if (!fieldVo.getName().startsWith("`")) {
                fieldVo.setName("`".concat(fieldVo.getName()).concat("`"));
            }
        });
        if (CollectionUtils.isEmpty(fieldVos)) {
            return Optional.empty();
        }
        StringBuilder builder = new StringBuilder();
        builder = builder.append("INSERT INTO ").append("`").append(tableVo.getName()).append("`").append("(");
        StringBuilder finalBuilder = builder;
        fieldVos.stream().forEach(fieldVo -> {
            finalBuilder.append(fieldVo.getName()).append(",");
        });
        builder = finalBuilder.replace(finalBuilder.length() - 1, finalBuilder.length(), ") VALUES ");
        builder = builder.append(supplier.get());
        return Optional.ofNullable(builder.toString());
    }


    public static class Builder {

        private TableVo tableVo;

        private List<FieldVo> fieldVos;

        private DataSourceConfiguration configuration;


        public Builder (TableVo tableVo, List<FieldVo> fieldVos) {
            this.tableVo = tableVo;
            this.fieldVos = fieldVos;
        }

        public Builder setTableVo(TableVo tableVo) {
            this.tableVo = tableVo;
            return this;
        }

        public Builder setFieldVos(List<FieldVo> fieldVos) {
            this.fieldVos = fieldVos;
            return this;
        }

        public Builder setConfiguration(DataSourceConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public SqlMaker build() {
            return new SqlMaker(this);
        }
    }

}
