package com.easyapi.base.enums;

/**
 * <p>可以简单理解该类是系统最基本最常用的错误码提示（正常错误码占用范围为：0～100还有一个500（未知具体错误）即可）
 * <p>具体错误业务错误并不会在该错误提示中体现出来
 * <p>是对系统基本业务错误定义，只是包含一些通用错误提示，并不会涵盖全部业务错误码
 * 根据不同的业务请在自定义项目上自行实现，实现{@link BaseEnum}即可
 *
 * @Author: jurentiancheng
 * @date 2022-04-21 18:30:05
 */
public enum ResultMessage implements BaseEnum<Integer> {
    /**
     * 系统通用返回码
     */
    SUCCESS(0, "success"),
    BASE_PARAM_ERROR(1, "请求参数异常"),
    BASE_QUERY_PARAM_IS_EMPTY_ERROR(2, "请求参数为空"),
    BASE_CHOOSE_OPERATOR_DATA_ERROR(3, "请选择一笔数据，再操作"),
    BASE_START_TIME_NOT_EMPTY_ERROR(4, "开始时间不能为空"),
    BASE_END_TIME_NOT_EMPTY_ERROR(5, "结束时间不能为空"),
    BASE_DB_EXIST_DATA_ERROR(6, "已存在对应信息"),
    BASE_BEAN_COPY_ERROR(7, "对象转换异常"),
    BASE_BATCH_BEAN_COPY_ERROR(8, "批量对象转换异常"),
    BASE_DATA_NULL_ERROR(9, "数据为空"),
    BASE_DATA_NOT_NULL_ERROR(10, "数据不为空"),
    BASE_STR_EMPTY_ERROR(11, "字符串为空"),
    BASE_STR_NOT_EMPTY_ERROR(12, "字符串不能为空"),
    BASE_DEVICE_URL_ERROR(13, "获取实时流地址异常"),
    BASE_ALL_DEVICE_ERROR(14, "获取设备失败"),
    BASE_NOT_FOUND_ENUM_ERROR(15, "非法编码"),

    REMOTER_SERVICE_ERROR(70, "请求%服务异常"),
    REMOTER_RESULT_ERROR(71, "%服务返回结果异常"),
    REMOTER_BASE_SERVICE_ERROR(72, "请求服务异常，可能网络失败"),
    REMOTER_REQUEST_ERROR(73, "请求%s平台服务异常:%s--%s"),
    REMOTER_REQUEST_RESULT_ERROR(74, "%s平台服务返回失败：%s"),
    FAILURE(500, "系统错误"),
    ;

    /**
     * 返回码
     */
    private final Integer code;

    /**
     * 信息
     */
    private final String message;

    ResultMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultMessage ofValue(Integer code) {
        return BaseEnum.get(ResultMessage.values(), code);
    }

    @Override
    public Integer ofCode() {
        return this.code;
    }

    @Override
    public String ofMessage() {
        return this.message;
    }
}