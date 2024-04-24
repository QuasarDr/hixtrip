package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.app.strategy.PaymentCallbackProcessor;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.sample.vo.OrderVO;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDomainService orderDomainService;

    @Autowired
    private PaymentCallbackProcessor paymentCallbackProcessor;

    @Override
    public OrderVO order(CommandOderCreateDTO commandOderCreateDTO) {
        Order order = new Order();
        order.setSkuId(commandOderCreateDTO.getSkuId());
        order.setAmount(commandOderCreateDTO.getAmount());
        order = orderDomainService.createOrder(order);
        //code 和message不知道哪来的，先不处理
        return OrderConvertor.INSTANCE.orderToOrderVo(order);
    }
}
