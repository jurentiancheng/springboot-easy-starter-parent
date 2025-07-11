package com.easyapi.base.helper;

import cn.hutool.core.date.DatePattern;

import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * 主要处理了时间格式字符串内容；以及提供了对时间格式字符串常见格式的方法替换
 * 除了提供特定的转换；也可以使用{@link DateConstantHelper#replace(String, Object...)}自行处理
 * {@link DatePattern}已经提供很多定义
 * @Author: jurentiancheng
 * @date 2021/12/7
 */
public class DateConstantHelper extends DatePattern {

    /**
     * 一天毫秒数
     */
    public static final int DAY_MILLISECONDS = 1000 * 60 * 60 * 24;
    /**
     * +8个时区
     */
    public static final String CN_GMT_TIME_ZONE = "GMT+8";
    public static final String CN_TIME_ZONE = "+8";
    public static final ZoneOffset CN_ZONE_OFFSET = ZoneOffset.of(DateConstantHelper.CN_TIME_ZONE);
    /**
     * 系统默认时区
     */
    public static final ZoneId ZONEID = ZoneId.systemDefault();

    /**
     * 匹配型日期格式；对于年月日的通配符是一样处理，对于时分秒的通配符是一样处理；可以参考{@link DateConstantHelper#dateTimeReplace(String, String)}来完成。</p>
     * DateConstant.dateTimeReplace("-",":"),返回结果就是：yyyy-MM-dd HH:mm:ss
     * 如果要逐个替换；请自行实现。
     */
    public static final String DATE_TIME_PATTERN = "yyyy%sMM%sdd HH%smm%sss";
    /**
     * 匹配型日期格式
     * 可以通过方法{@link DateConstantHelper#dateReplace}来替换相关符号；</p>
     * DateConstant.dateReplace("-"),返回结果就是：yyyy-MM-dd
     */
    public static final String DATE_PATTERN = "yyyy%sMM%sdd";
    /**
     * 匹配型时间格式
     */
    public static final String TIME_PATTERN = "HH%smm%sdd";


    private DateConstantHelper() {
        throw new IllegalStateException("DateConstantHelper Utility class");
    }

    /**
     * 替换{@link DateConstantHelper#DATE_TIME_PATTERN}格式方法
     *
     * @param dateReplace 年月日的替换符号
     * @param timeReplace 时分秒的替换符号
     * @return 替换后的时间格式
     */
    public static String dateTimeReplace(String dateReplace, String timeReplace) {
        return replace(DATE_TIME_PATTERN, dateReplace, dateReplace, timeReplace, timeReplace);
    }

    /**
     * 替换{@link DateConstantHelper#DATE_PATTERN}格式方法
     *
     * @param dateReplace 年月日的替换符号
     * @return 替换后的时间格式
     */
    public static String dateReplace(String dateReplace) {
        return replace(DATE_PATTERN, dateReplace, dateReplace);
    }

    /**
     * 替换{@link DateConstantHelper#TIME_PATTERN}格式方法
     *
     * @param dateReplace 年月日的替换符号
     * @return 替换后的时间格式
     */
    public static String timeReplace(String dateReplace) {
        return replace(TIME_PATTERN, dateReplace, dateReplace);
    }

    /**
     * 使用该方法或者使用String.format()直接转换
     *
     * @param dateString  时间格式比如{@link DateConstantHelper#DATE_TIME_PATTERN}、{@link DateConstantHelper#DATE_PATTERN}、{@link DateConstantHelper#TIME_PATTERN}或则其他时间格式
     * @param dateReplace 替换格式；待替换的长度需已dateString中的占位符个数需一致
     * @return 替换后的时间格式
     */
    public static String replace(String dateString, Object... dateReplace) {
        return String.format(dateString, dateReplace);
    }
}
