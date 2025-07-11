package com.easyapi.base;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 返回分页信息
 *
 * @Author: jurentiancheng
 * @date 2021-09-07 14:26:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedInfo implements Serializable {
    @ApiParam(value = "当前页", example = "1")
    private int page;
    @ApiParam(value = "每页的数量", example = "10")
    private int size;
    @ApiParam(value = "总数", example = "10")
    private long total;
}