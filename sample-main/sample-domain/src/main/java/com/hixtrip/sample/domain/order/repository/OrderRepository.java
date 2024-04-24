package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.model.CommandPay;

/**
 *
 */
public interface OrderRepository {

    Order createOrder(Order order);

    Order orderPaySuccess(CommandPay commandPay);

    Order orderPayFail(CommandPay commandPay);
}
