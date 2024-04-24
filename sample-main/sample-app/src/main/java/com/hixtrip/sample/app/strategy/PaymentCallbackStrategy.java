package com.hixtrip.sample.app.strategy;

import com.hixtrip.sample.client.order.dto.CommandPayDTO;


public interface PaymentCallbackStrategy {

    void handle(CommandPayDTO commandPayDTO);

}
