package com.easyapi.basembp.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.easyapi.base.enums.BaseEnum;

/**
 * <p>删除标志
 *
 * @Author: jurentiancheng
 * @date 2022-09-21 18:30:05
 */
public enum IsDel implements BaseEnum<Integer>, IEnum<Integer> {
    /**
     * 系统通用返回码
     */
    NORMAL(0, "正常"),
    DELETED(-1, "删除");;

    /**
     * 返回码
     */
    private final Integer code;

    /**
     * 信息
     */
    private final String message;

    IsDel(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static IsDel ofValue(Integer code) {
        return BaseEnum.get(IsDel.values(), code);
    }

    @Override
    public Integer ofCode() {
        return this.code;
    }

    @Override
    public String ofMessage() {
        return this.message;
    }

    @Override
    public Integer getValue() {
        return this.code;
    }
}