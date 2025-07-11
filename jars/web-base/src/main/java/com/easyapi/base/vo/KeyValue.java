package com.easyapi.base.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * key-value对象值
 *
 * @Author: jurentiancheng
 * @date 2022/10/25
 */
@Data
public class KeyValue<K, V> implements Serializable {
    private K key;
    private V value;
}
