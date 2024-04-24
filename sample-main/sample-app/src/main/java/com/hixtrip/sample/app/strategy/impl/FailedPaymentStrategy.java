package com.hixtrip.sample.app.strategy.impl;

import com.hixtrip.sample.app.convertor.CommandConvertor;
import com.hixtrip.sample.app.strategy.PaymentCallbackStrategy;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FailedPaymentStrategy implements PaymentCallbackStrategy {

    @Autowired
    private OrderDomainService orderDomainService;
    @Autowired
    private PayDomainService payDomainService;

    @Override
    public void handle(CommandPayDTO commandPayDTO) {
        CommandPay commandPay = CommandConvertor.INSTANCE.commandPayDtoToCommandPay(commandPayDTO);
        orderDomainService.orderPayFail(commandPay);
        payDomainService.payRecord(commandPay);
    }
}