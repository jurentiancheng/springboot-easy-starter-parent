package com.easyapi.base;

import com.easyapi.base.vo.UserBaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 附带操作人员操作系统操作信息，需要对操作人员的操作内容来处理相关日志；可使用此类作为操作的基类。
 *
 * @Author: jurentiancheng
 * @date 2021/12/7
 */
@Data
public class Operator implements Serializable {

    /**
     * 操作人员id
     */
    @ApiModelProperty(hidden = true)
    private Long operatorId;

    /**
     * 操作人员名称；正常情况应该使用不上这个字段来操作更新数据库；
     */
    @ApiModelProperty(hidden = true)
    private String operatorName;

    /**
     * 用户和组织基本信息
     */
    @ApiModelProperty(hidden = true)
    private UserBaseInfo userSession;
}
