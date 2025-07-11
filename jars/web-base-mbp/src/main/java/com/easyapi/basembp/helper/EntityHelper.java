package com.easyapi.basembp.helper;

import com.easyapi.basembp.BaseEntity;
import com.easyapi.basembp.enums.IsDel;

import java.util.Objects;
import java.util.Optional;

/**
 * 对数据库数据更新或者创建需要对基本create/update信息操作类
 *
 * @Author: jurentiancheng
 * @date 2022/9/27
 */
public class EntityHelper {

    /**
     * 为该数据设置正常标识
     *
     * @param entity 数据库操作实体对象
     * @param <T>    泛型
     */
    private static <T extends BaseEntity> void fillNormal(T entity) {
        if (Objects.isNull(entity)) {
            return;
        }
        entity.setIsDel(Long.valueOf(IsDel.NORMAL.ofCode()));
    }

    /**
     * 为该数据设置删除标识，并且删除值设置为该对象id值  = id * (-1)；
     *
     * @param entity 数据库操作实体对象
     * @param <T>    泛型
     */
    private static <T extends BaseEntity> void fillDel(T entity) {
        if (Objects.isNull(entity) || entity.getId() == null) {
            return;
        }
        entity.setIsDel(entity.getId() * IsDel.DELETED.ofCode());
    }

    /**
     * 根据IsDel值设置该数据删除标志是否正常；
     * <p>如果设置正常可以直接使用{@link EntityHelper#fillNormal(BaseEntity)};
     * <p>如果设置删除可以直接使用{@link EntityHelper#fillDel(BaseEntity)} ;
     *
     * @param entity 数据库操作实体对象
     * @param isDel  删除标志
     * @param <T>    泛型
     */
    private static <T extends BaseEntity> void fillIsDel(T entity, IsDel isDel) {
        if (IsDel.NORMAL.ofCode().equals(isDel.ofCode())) {
            fillNormal(entity);
        } else {
            fillDel(entity);
        }
    }

    /**
     * 对于新增要对数据中create/update信息进行更新
     *
     * @param entity     数据库操作实体对象(数据记录)
     * @param operatorId 数据操作者（更新或者创建）
     * @param <T>        泛型
     * @return 返回entity
     */
    public static <T extends BaseEntity> T fillDataCreate(T entity, Long operatorId) {
        if (Objects.isNull(entity)) {
            return null;
        }
        Long realBy = Optional.ofNullable(operatorId).orElse(0L);
        entity.setCreateBy(realBy);
        entity.setUpdateBy(realBy);
        entity.setIsDel(Long.valueOf(IsDel.NORMAL.ofCode()));
        return entity;
    }

    /**
     * 数据删除逻辑
     *
     * @param entity     数据库对应数据值；是继承{@link BaseEntity}
     * @param operatorId 操作该数据操作者
     * @param <T>        泛型
     */
    public static <T extends BaseEntity> void fillDataDel(T entity, Long operatorId) {
        fillDataUpdate(entity, operatorId);
        fillDel(entity);
    }

    /**
     * 数据被更新时，更新操作者
     *
     * @param entity     数据库对应数据值；是继承{@link BaseEntity}
     * @param operatorId 操作该数据操作者
     * @param <T>        泛型
     */
    public static <T extends BaseEntity> void fillDataUpdate(T entity, Long operatorId) {
        if (Objects.isNull(entity)) {
            return;
        }
        Long realBy = Optional.ofNullable(operatorId).orElse(0L);
        entity.setUpdateBy(realBy);
    }

    /**
     * 对于更新要对数据中update信息进行更新
     *
     * @param entity     数据库操作实体对象(数据记录)
     * @param operatorId 数据操作者（更新或者创建）
     * @param isDel      数据状态标识位
     * @param <T>        泛型
     */
    public static <T extends BaseEntity> void fillDataUpdate(T entity, Long operatorId, IsDel isDel) {
        fillDataUpdate(entity, operatorId);
        fillIsDel(entity, isDel);
    }
}
