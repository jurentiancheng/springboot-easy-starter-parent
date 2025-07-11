package com.easyapi.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 按 Date类型周期类；{@link java.time.LocalDateTime}类型请参考{@link CircleLocalDateTime}
 * @Author: jurentiancheng
 * @date 2021/12/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CircleDate implements Serializable {
    /**
     * 周期开始时间
     */
    private Date start;

    /**
     * 周期结束时间
     */
    private Date end;
}



