package com.easyapi.jdbc;

import java.util.List;

/**
 * @Author: jurentiancheng
 * @Date: 2021/5/8 10:07 上午
 * @Version 1.0
 */
@FunctionalInterface
public interface ISql<T> {

    List<T> accept(String sql);
}
