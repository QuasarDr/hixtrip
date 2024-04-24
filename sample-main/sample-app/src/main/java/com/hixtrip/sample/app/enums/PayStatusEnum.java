package com.hixtrip.sample.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum PayStatusEnum {
    //假设回调的status为如下几个，重复支付也是状态的一种
    SUCCESS("SUCCESS", "successfulPaymentStrategy"),
    FAIL("FAIL", "failedPaymentStrategy"),
    DUPLICATE("DUPLICATE", "duplicatePaymentStrategy"),

    ;


    private String status;

    private String beanName;
}
