package com.easyapi.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: jurentiancheng
 * @date 2021/12/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CircleLocalDateTime implements Serializable {
    private LocalDateTime start;
    private LocalDateTime end;
}



