package com.easyapi.kafka.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonHandler extends BaseTypeHandler<JSONObject> {

	public JsonHandler() {

	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.toJSONString());
	}

	@Override
	public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String i = rs.getString(columnName);
		if (rs.wasNull()) {
			return null;
		} else {
			return JSON.parseObject(i);
		}
	}

	@Override
	public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String i = rs.getString(columnIndex);
		if (rs.wasNull()) {
			return null;
		} else {
			return JSON.parseObject(i);
		}
	}

	@Override
	public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String i = cs.getString(columnIndex);
		if (cs.wasNull()) {
			return null;
		} else {
			return JSON.parseObject(i);
		}
	}
}