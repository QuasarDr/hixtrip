package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.infra.db.convertor.OrderDOConvertor;
import com.hixtrip.sample.infra.db.dataobject.OrderDO;
import com.hixtrip.sample.infra.db.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private OrderMapper orderMapper;


    @Override
    public Order createOrder(Order order) {
        OrderDO orderDO = OrderDOConvertor.INSTANCE.domainToDo(order);
        orderMapper.insert(orderDO);
        return OrderDOConvertor.INSTANCE.doToDomain(orderDO);
    }

    @Override
    public Order orderPaySuccess(CommandPay commandPay) {
        OrderDO orderDO = orderMapper.selectById(commandPay.getOrderId());
        orderDO.setPayStatus(commandPay.getPayStatus());
        orderMapper.updateById(orderDO);

        return OrderDOConvertor.INSTANCE.doToDomain(orderDO);
    }

    @Override
    public Order orderPayFail(CommandPay commandPay) {
        //跟支付成功一样修改下传过来的标识，不清楚标识的种类，先不做其他处理
        OrderDO orderDO = orderMapper.selectById(commandPay.getOrderId());
        orderDO.setPayStatus(commandPay.getPayStatus());
        orderMapper.updateById(orderDO);

        return OrderDOConvertor.INSTANCE.doToDomain(orderDO);
    }
}
