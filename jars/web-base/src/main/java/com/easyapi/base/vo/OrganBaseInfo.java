package com.easyapi.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: jurentiancheng
 * @date 2022/10/19
 */
@Data
@ApiModel("组织信息")
public class OrganBaseInfo implements Serializable {

    @ApiModelProperty(value = "id",hidden = true)
    private Integer id;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码",hidden = true)
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty(value = "组织名称",hidden = true)
    private String name;

    /**
     * 全路径
     */
    @ApiModelProperty(value = "组织全路径",hidden = true)
    private String path;
}
