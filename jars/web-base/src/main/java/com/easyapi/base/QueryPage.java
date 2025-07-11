package com.easyapi.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;

/**
 * 基本分页查询类；如果需要附加操作人员的信息可使用{@link OpQueryPage}来使用
 *
 * @Author: jurentiancheng
 * @date 2021-09-07 14:26:43
 */
@Data
public class QueryPage implements Serializable {
    @ApiParam(value = "当前页", example = "1")
    private int page = 1;
    @ApiParam(value = "每页的数量", example = "20")
    private int size = 20;
}