package com.easyapi.base.exception;

import com.easyapi.base.enums.BaseEnum;
import com.easyapi.base.enums.ResultMessage;
import lombok.Data;

/**
 * 业务异常;相关业务错误码{@link BaseEnum}
 *
 * @Author: jurentiancheng
 * @date 2022-09-21 18:30:05
 */
@Data
public class BusinessException extends RuntimeException {
    private String msg;
    private int code;

    public BusinessException() {
        super();
    }

    /**
     * 通过错误信息创建业务异常对象。使用默认错误码ResultMessage.FAILURE.ofCode()
     *
     * @param message 错误提示语
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultMessage.FAILURE.ofCode();
    }

    /**
     * 通过错误信息，错误码创建业务异常对象
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 通过错误信息，错误码创建业务异常对象；如果不满足可以自行=实现{@link BaseEnum}该接口；定义自己业务码即可；
     *
     * @param message 错误码信息汇总类
     */
    public BusinessException(BaseEnum<Integer> message) {
        super(message.ofMessage());
        this.code = message.ofCode();
    }

    public BusinessException(BaseEnum<Integer> message, Throwable cause) {
        super(message.ofMessage(), cause);
        this.code = message.ofCode();
    }

    /**
     * 通过错误信息，异常对象，错误码创建业务异常对象
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 通过异常对象，错误码创建业务异常对象
     */
    public BusinessException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }
}
