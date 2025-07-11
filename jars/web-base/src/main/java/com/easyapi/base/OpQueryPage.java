package com.easyapi.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 附带上操作人员的请求分页类；实际上与{@link QueryPage}相差无几；如果不需要附带上操作人员信息可使用{@link QueryPage}
 * @Author: jurentiancheng
 * @date 2021/12/7
 */
@Data
public class OpQueryPage extends Operator {
    @ApiParam(value = "当前页", example = "1")
    private int page = 1;
    @ApiParam(value = "每页的数量", example = "20")
    private int size = 20;
}
