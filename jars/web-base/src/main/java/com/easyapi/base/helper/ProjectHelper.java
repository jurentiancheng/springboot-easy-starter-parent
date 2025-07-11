package com.easyapi.base.helper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 项目助手
 *
 * @author liuxuan
 * @date 2022/12/8
 */
public class ProjectHelper {

    /**
     * 列表按照指定字段映射为Map
     *
     * @param list         列表
     * @param unionKeyFunc 联盟关键函数
     * @return {@link Map}<{@link K}, {@link V}>
     */
    public static <K,V> Map<K, V> listToMap(List<V> list, Function<V, K> unionKeyFunc) {
        return list.stream().collect(Collectors.toMap(unionKeyFunc, v -> v, (v1, v2) -> v2));
    }

    /**
     *
     * @param list          数据集合
     * @param unionKeyFunc  标识Key
     * @param valueFunc     目标字段
     * @return
     * @param <K>
     * @param <V>
     * @param <T>
     */
    public static <K,V,T> Map<K, T> listToMap(List<V> list, Function<V, K> unionKeyFunc, Function<V,T> valueFunc) {
        return list.stream().collect(Collectors.toMap(unionKeyFunc, valueFunc));
    }

    /**
     *
     * @param list          数据集合
     * @param unionKeyFunc  标识Key
     * @param valueFunc     目标字段
     * @param filterPred    过滤条件
     * @return
     * @param <K>
     * @param <V>
     * @param <T>
     */
    public static <K,V,T> Map<K, T> listToMap(List<V> list, Function<V, K> unionKeyFunc, Function<V,T> valueFunc, Predicate<V> filterPred) {
        Stream<V> stream = list.stream();
        if (filterPred != null) {
            stream = stream.filter(filterPred);
        }
        return stream.collect(Collectors.toMap(unionKeyFunc, valueFunc));
    }

    /**
     * 取出列表指定字段的集合
     *
     * @param list         列表
     * @param unionKeyFunc 联盟关键函数
     * @return {@link List}<{@link K}>
     */
    public static <K,T> List<K> listWith(List<T> list, Function<T, K> unionKeyFunc) {
       return listWith(list, unionKeyFunc, null);
    }

    /**
     * 取出列表指定字段的集合
     *
     * @param list         列表
     * @param unionKeyFunc 联盟关键函数
     * @return {@link List}<{@link K}>
     */
    public static <K,T> List<K> listWith(List<T> list, Function<T, K> unionKeyFunc, Predicate<T> filterPredicate) {
        Stream<T> stream = list.stream();
        if (filterPredicate != null) {
            stream = stream.filter(filterPredicate);
        }
        return stream.map(unionKeyFunc).distinct().collect(Collectors.toList());
    }

    /**
     * 此方法用于填充List<T>集合中，根据T对象中某个属性值 查找内容，并填充到 List<T>中
     * 主要用户根据结合对象的id/code查找 name 再填充结合的场景
     * @param inputVos           输入的集合对象
     * @param inputKeyMapper     输入集合中字段映射
     * @param sourceByKeyMapper  输入字段获取的集合映射
     * @param sourceKeyMapper    获取的集合中，Map key字段映射
     * @param inputIterateConsumer  输入的集合对象遍历
     */
    public static <T,K,U> void fillVoExtraData(List<T> inputVos,
                                                 Function<T, K> inputKeyMapper,
                                                 Function<List<K>, List<U>> sourceByKeyMapper,
                                                 Function<U, K> sourceKeyMapper,
                                                 Consumer<Map<K, U>> inputIterateConsumer) {
        if (CollectionUtil.isEmpty(inputVos)) {
            return;
        }
        List<K> fieldVos = ProjectHelper.listWith(inputVos, inputKeyMapper);
        if (fieldVos.isEmpty()) {
            return;
        }
        List<U> field2Vos = sourceByKeyMapper.apply(fieldVos);
        if (field2Vos.isEmpty()) {
            return;
        }
        Map<K, U> kuMap = ProjectHelper.listToMap(field2Vos, sourceKeyMapper);
        if (ObjectUtil.isEmpty(kuMap)) {
            return;
        }
        inputIterateConsumer.accept(kuMap);
    }


    /**
     * 此方法用于填充List<T>集合中，根据T对象中某个属性值 查找内容，并填充到 List<T>中
     * 主要用户根据结合对象的id/code查找 name 再填充结合的场景
     * @param inputVos                   输入的集合对象
     * @param inputKeyMapper             输入集合中字段映射
     * @param sourceByKeyToMapMapper     输入字段获取的集合并转换为Map映射
     * @param inputIterateConsumer       输入的集合对象遍历
     * @param <T>
     * @param <K>
     * @param <U>
     */
    public static <T,K,U> void fillVoExtraData(List<T> inputVos,
                                             Function<T, K> inputKeyMapper,
                                             Function<List<K>, Map<K, U>> sourceByKeyToMapMapper,
                                             Consumer<Map<K, U>> inputIterateConsumer) {
        if (CollectionUtil.isEmpty(inputVos)) {
            return;
        }
        List<K> fieldVos = ProjectHelper.listWith(inputVos, inputKeyMapper);
        if (fieldVos.isEmpty()) {
            return;
        }
        Map<K, U> field2Vos = sourceByKeyToMapMapper.apply(fieldVos);
        if (field2Vos.isEmpty()) {
            return;
        }
        inputIterateConsumer.accept(field2Vos);
    }

    /**
     * 此方法用于填充List<T>集合中，根据T对象中某个属性值 查找内容，并填充到 List<T>中
     * 主要用户根据结合对象的id/code查找 name 再填充结合的场景
     * @param inputVos           输入的集合对象
     * @param inputKeyMapper     输入集合中字段映射
     * @param sourceByKeyMapper  输入字段获取的集合映射
     * @param sourceKeyMapper    获取的集合中，Map key字段映射
     * @param sourceValueMapper  获取的集合中，Map value字段映射
     * @param inputIterateConsumer  输入的集合对象遍历
     */
    public static <T,K,U,V> void fillVoExtraData(List<T> inputVos,
                                                 Function<T, K> inputKeyMapper,
                                                 Function<List<K>, List<U>> sourceByKeyMapper,
                                                 Function<U, K> sourceKeyMapper,
                                                 Function<U, V> sourceValueMapper,
                                                 Consumer<Map<K, V>> inputIterateConsumer) {
        if (CollectionUtil.isEmpty(inputVos)) {
            return;
        }
        List<K> fieldVos = ProjectHelper.listWith(inputVos, inputKeyMapper);
        if (fieldVos.isEmpty()) {
            return;
        }
        List<U> field2Vos = sourceByKeyMapper.apply(fieldVos);
        if (field2Vos.isEmpty()) {
            return;
        }
        Map<K, V> vuMap = ProjectHelper.listToMap(field2Vos, sourceKeyMapper, sourceValueMapper);
        if (ObjectUtil.isEmpty(vuMap)) {
            return;
        }
        inputIterateConsumer.accept(vuMap);
    }



}
