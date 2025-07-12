package com.easyapi.basembp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据库实体类基类；基本几个字段定义是固定要求；
 * id是Integer 自增长 / Long snowflakeId，is_del删除标志，createBy创建者，updateBy最后更新者
 *
 * @Author: jurentiancheng
 * @date 2022/9/27
 */
@Data
public class BaseEntity implements Serializable {
    /**
     * 数据唯一标识
     */
    private Long id;
    /**
     * 删除标识
     */
    private Long isDel;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
