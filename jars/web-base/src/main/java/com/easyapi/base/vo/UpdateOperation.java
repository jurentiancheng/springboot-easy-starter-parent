package com.easyapi.base.vo;

import com.easyapi.base.Operator;
import lombok.Data;

/**
 * @Author: jurentiancheng
 * @date 2022/9/27
 */
@Data
public class UpdateOperation<D, C> extends Operator {

    /**
     * 更新内容信息
     */
    private D data;

    /**
     * 更新条件
     */
    private C condition;
}
