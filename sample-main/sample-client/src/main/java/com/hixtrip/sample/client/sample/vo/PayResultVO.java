package com.hixtrip.sample.client.sample.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 这是返回值的示例
 */
@Data
@Builder
@AllArgsConstructor
public class PayResultVO {

    private String code;
    private String msg;
}
