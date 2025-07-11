package com.easyapi.kafka.handler;

import com.alibaba.fastjson.JSON;
import com.easyapi.basembp.PersistObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: jurentiancheng
 * @date 2022/10/25
 */
public class PojoJsonHandler<E extends PersistObject> extends BaseTypeHandler<E> {
    private Class<E> type;

    public PojoJsonHandler() {
    }

    public PojoJsonHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, PersistObject persistObject, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(persistObject));
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String result = resultSet.getString(columnName);
        if (resultSet.wasNull()) {
            return null;
        }
        return JSON.parseObject(result, type);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        if (resultSet.wasNull()) {
            return null;
        }
        return JSON.parseObject(result, type);
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        if (callableStatement.wasNull()) {
            return null;
        }
        return JSON.parseObject(result, type);
    }
}
