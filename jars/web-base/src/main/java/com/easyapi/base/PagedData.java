package com.easyapi.base;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


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
public class PagedData<T> implements Serializable {
    @ApiParam(value = "分页数据：分页具体列表信息，请查看具体类说明", example = "List")
    private List<T> pageData;
    @ApiParam(value = "分页信息：当前页、每页条数、总条数", example = "Object")
    private PagedInfo pagedInfo;

    public void setPagedInfo(int page, int size, int total) {
        this.pagedInfo = PagedInfo.builder().page(page).size(size).total(total).build();
    }
}