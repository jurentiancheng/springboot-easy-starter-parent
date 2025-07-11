package com.easyapi.base;

import com.easyapi.base.enums.BaseEnum;
import com.easyapi.base.enums.ResultMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回体
 *
 * @Author: jurentiancheng
 * @date 2021-09-07 14:26:43
 */
@Data
@ApiModel(value = "返回结果:0为正确,其他未业务错误代码")
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ERROR_CODE = 500;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T data;
    /**
     * 返回码
     */
    @ApiModelProperty(value = "返回码:0表示成功，其他表示失败")
    private int code = 0;
    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息")
    private String message = "success";

    /**
     * 默认为成功
     */
    public Result() {
        this.code = ResultMessage.SUCCESS.ofCode();
        this.message = ResultMessage.SUCCESS.ofMessage();
    }

    /**
     * 对请求返回结果信息设置
     *
     * @param code    请求结果的code值
     * @param message 请求结果的业务信息
     */
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(BaseEnum<Integer> baseEnum) {
        this.code = baseEnum.ofCode();
        this.message = baseEnum.ofMessage();
    }

    /**
     * 请求成功
     *
     * @param <T> 业务数据类型
     * @return 处理业务成功所返回给调用方的业务结果说明
     */
    public static <T> Result<T> toSuccess() {
        return new Result<>();
    }

    /**
     * 请求成功，并设置特定的提示语。
     * <p>特定说明，该方法为何与其他toSuccess命名名称不一样，该方法主要是设置特定的成功业务提示语。假设该方法命名也是toSuccess(String message),
     * 那么会与{@link Result#toSuccess(Object)}的data也是String类型,这样导致两个方法定义一样（具体实现不一样），对于jvm来说无法识别。
     *
     * @param msg 业务操作成功，返回正确提示语
     * @param <T> 业务数据类型
     * @return 处理业务成功所返回给调用方的业务结果说明
     */
    public static <T> Result<T> toSuccessMsg(String msg) {
        return new Result<T>().setMessage(msg);
    }

    /**
     * 请求成功，并设置特定业务数据。
     *
     * @param data 业务数据
     * @param <T>  业务数据类型
     * @return 处理业务成功所返回给调用方的业务结果说明
     */
    public static <T> Result<T> toSuccess(T data) {
        Result<T> result = toSuccess();
        return result.setData(data);
    }

    /**
     * 请求成功，并设置特定的提示语和具体业务数据。
     *
     * @param msg  提示语
     * @param data 业务数据
     * @param <T>  业务数据类型
     * @return 处理业务成功所返回给调用方的业务结果说明
     */
    public static <T> Result<T> toSuccess(String msg, T data) {
        Result<T> tResult = new Result<>();
        tResult.setMessage(msg).setData(data);
        return tResult;
    }

    /**
     * 处理业务失败，并提示相关业务失败原因
     *
     * @param <T> 业务数据类型
     * @return 处理业务失败所返回给调用方的失败原因
     */
    public static <T> Result<T> toError() {
        return Result.toError(ResultMessage.FAILURE);
    }

    public static <T> Result<T> toError(BaseEnum<Integer> baseEnum) {
        return new Result<>(baseEnum);
    }

    /**
     * 处理业务失败，并返回相关业务失败数据。正常在业务上使用不到改方法，比较特殊情况吧。
     *
     * @param data 业务数据
     * @param <T>  业务数据类型
     * @return 处理业务失败所返回给调用方的失败原因
     */
    public static <T> Result<T> toError(T data) {
        Result<T> result = toError();
        return result.setData(data);
    }

    /**
     * 处理业务失败，并返回相关业务失败信息。
     *
     * @param code   业务错误码
     * @param errMsg 业务错误提示语
     * @param <T>    业务数据类型
     * @return 处理业务失败所返回给调用方的失败原因
     */
    public static <T> Result<T> toError(int code, String errMsg) {
        return new Result<>(code, errMsg);
    }

    /**
     * 处理业务失败，并设置相关业务错误提示语
     *
     * @param errMsg 业务错误提示
     * @param <T>    数据数据类型
     * @return 处理业务失败所返回给调用方的失败原因
     */
    public static <T> Result<T> toErrorMsg(String errMsg) {
        return new Result<T>().setCode(ERROR_CODE).setMessage(errMsg);
    }

    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }
}
