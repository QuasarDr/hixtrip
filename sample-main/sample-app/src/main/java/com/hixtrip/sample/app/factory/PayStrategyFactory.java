package com.hixtrip.sample.app.factory;

import com.hixtrip.sample.app.enums.PayStatusEnum;
import com.hixtrip.sample.app.strategy.PaymentCallbackStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 支付回调工厂类
 */
@Slf4j
@Component
public class PayStrategyFactory {

    @Resource
    private Map<String, PaymentCallbackStrategy> paymentCallbackStrategyMap;

    /**
     * 获取对应支付策略类
     *
     * @param payStrategyEnum 支付策略枚举
     */
    public PaymentCallbackStrategy getPayStrategy(PayStatusEnum payStrategyEnum) {

        if (!paymentCallbackStrategyMap.containsKey(payStrategyEnum.getBeanName())) {
            log.info("没有对应的支付回调策略！");
            throw new RuntimeException("没有对应的支付回调策略！");
        }

        return paymentCallbackStrategyMap.get(payStrategyEnum.getBeanName());
    }
}