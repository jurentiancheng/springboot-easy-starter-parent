package com.easyapi.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 只会放最基本信息；不会放全量用户信息字段信息
 * 包含用户最基本信息以及组织最基本信息；其他信息需要则通过feign方式调用即可
 * 最基本信息很大程度上是不会发生字段变更或者结构变更。即最大程度减少跟album层耦合。
 *
 * @Author: jurentiancheng
 * @date 2022/10/19
 */
@Data
@ApiModel("请求操作者用户信息")
public class UserBaseInfo implements Serializable {

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户id",hidden = true)
    private Long id;
    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称",hidden = true)
    private String username;
    /**
     * 用户类型
     */
    @ApiModelProperty(value = "用户类型",hidden = true)
    private String userType;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码",hidden = true)
    private String tenantCode;

    /**
     * 组织信息
     */
    @ApiModelProperty(value = "组织信息",hidden = true)
    private List<OrganBaseInfo> organizations;
}
