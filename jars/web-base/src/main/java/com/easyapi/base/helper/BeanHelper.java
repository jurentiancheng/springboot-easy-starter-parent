package com.easyapi.base.helper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.easyapi.base.enums.ResultMessage;
import com.easyapi.base.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Bean对象的copy操作
 *
 * @Author: jurentiancheng
 * @date 2022/9/13
 */
public class BeanHelper extends BeanUtil {

    private BeanHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * list类型转换属性复制
     *
     * @param sourceList  原list
     * @param targetClass 目标对象类型
     * @param <T>         目标对象泛型
     * @return 目标对象list
     */
    public static <T, R> List<R> convertList(List<T> sourceList, Class<R> targetClass) {
        if (CollUtil.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        List<R> tList = new ArrayList<>(sourceList.size());
        try {
            for (Object sourceObject : sourceList) {
                tList.add(convertObj(sourceObject, targetClass));
            }
        } catch (Exception e) {
            throw new BusinessException(ResultMessage.BASE_BATCH_BEAN_COPY_ERROR, e);
        }
        return tList;
    }

    /**
     * @param sourceList      原数据集合
     * @param convertFunction 具体转换方式
     * @param <T>             原数据集合
     * @param <R>             结果集
     * @return 转换结果集合
     */
    public static <T, R> List<R> convertFunc(List<T> sourceList, Function<? super T, R> convertFunction) {
        if (CollUtil.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        return convertFunc(sourceList, Objects::nonNull, convertFunction);
    }

    /**
     * @param sourceList        原数据集合
     * @param conditionFunction 具体过滤方法
     * @param convertFunction   具体转换方式
     * @param <T>               原数据集合
     * @param <R>               结果集
     * @return 返回转换后结果
     */
    public static <T, R> List<R> convertFunc(List<T> sourceList, Predicate<? super T> conditionFunction, Function<? super T, R> convertFunction) {
        if (CollUtil.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        return sourceList.stream().filter(conditionFunction).map(convertFunction).collect(Collectors.toList());
    }


    /**
     * 对象类型转换属性复制
     *
     * @param source      原对象
     * @param targetClass 目标对象类型
     * @param <T>         目标对象泛型
     * @return 目标对象
     */
    public static <T> T convertObj(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            return copyProperties(source, targetClass);
        } catch (Exception e) {
            throw new BusinessException(ResultMessage.BASE_BEAN_COPY_ERROR, e);
        }
    }
}