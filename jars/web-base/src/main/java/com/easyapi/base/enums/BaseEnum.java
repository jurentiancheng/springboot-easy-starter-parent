package com.easyapi.base.enums;


import com.easyapi.base.exception.BusinessException;

/**
 * 基本枚举类型接口
 *
 * @Author: jurentiancheng
 * * @date 2021/12/7
 */
public interface BaseEnum<T> {

    /**
     * 根据code值返回具体枚举对象
     *
     * @param values 枚举对应说有一类定义全部值
     * @param code   需要查询得code
     * @param <K>    实际枚举类型
     * @return 返回枚举对象
     */
    static <K extends BaseEnum> K get(K[] values, Object code) {
        K orNull = BaseEnum.getOrNull(values, code);
        if (null != orNull) {
            return orNull;
        }
        throw new BusinessException(ResultMessage.BASE_NOT_FOUND_ENUM_ERROR.ofCode(), ResultMessage.BASE_NOT_FOUND_ENUM_ERROR.ofMessage() + ":" + code);
    }

    /**
     * 根据code返回对应的枚举信息如果不存在，则返回默认值defaultValue
     *
     * @param values       枚举类所有枚举值
     * @param code         code值
     * @param defaultValue 如果为null，则返回该值
     * @param <K>          类型
     * @return 返回枚举值
     */
    static <K extends BaseEnum> K getOrDefault(K[] values, Object code, K defaultValue) {
        K orNull = BaseEnum.getOrNull(values, code);
        if (null == orNull) {
            return defaultValue;
        }
        return orNull;

    }

    /**
     * 根据code返回对应的枚举信息，如果不存在返回null
     *
     * @param values 枚举类所有枚举值
     * @param code   code值
     * @param <K>    类型
     * @return 返回枚举值, 没有对应返回null
     */
    static <K extends BaseEnum> K getOrNull(K[] values, Object code) {
        if (values == null || values.length <= 0) {
            return null;
        }
        if (code == null) {
            return null;
        }
        for (K value : values) {
            if (value.ofCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 枚举对应的code值;正常来说只会是Integer,String两种类型；
     *
     * @return 返回code值
     */
    T ofCode();

    /**
     * 枚举的code值对应描述说明
     *
     * @return 返回code值对应描述说明
     */
    String ofMessage();
}