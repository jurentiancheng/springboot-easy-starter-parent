package com.easyapi.kafka.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonArrayHandler extends BaseTypeHandler<JSONArray> {

    public JsonArrayHandler() {

    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toJSONString());
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String i = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return JSON.parseArray(i);
        }
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String i = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return JSON.parseArray(i);
        }
    }

    @Override
    public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String i = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return JSON.parseArray(i);
        }
    }
}