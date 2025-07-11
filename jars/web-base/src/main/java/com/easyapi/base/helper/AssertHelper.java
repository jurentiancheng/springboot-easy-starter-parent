package com.easyapi.base.helper;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.easyapi.base.enums.BaseEnum;
import com.easyapi.base.enums.ResultMessage;
import com.easyapi.base.exception.BusinessException;

/**
 * 业务断言工具类
 *
 * @author lijf
 * @date 2022/9/29
 */
public class AssertHelper extends Assert {

    public AssertHelper() {
        //  document why this constructor is empty
    }

    /**
     * 如果为{@code null} 抛出异常
     *
     * @param t   待判断对象
     * @param <T> 对象泛型
     */
    public static <T> void assertNotNull(T t) {
        assertNotNull(t, ResultMessage.BASE_DATA_NULL_ERROR);
    }

    /**
     * 如果为{@code null} 抛出异常，并带上特定业务错误信息
     *
     * @param t       待判断对象
     * @param message 业务错误信息
     * @param <T>     对象泛型
     */
    public static <T> void assertNotNull(T t, BaseEnum<Integer> message) {
        notNull(t, () -> new BusinessException(message));
    }

    /**
     * 字符串为空,抛出异常
     *
     * @param str     字符串
     * @param message 业务错误码
     */
    public static void assertNotEmpty(String str, BaseEnum<Integer> message) {
        isFalse(StrUtil.isBlank(str), () -> new BusinessException(message));
    }
}
