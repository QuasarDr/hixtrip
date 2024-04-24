package com.hixtrip.sample.domain.order;

import com.hixtrip.sample.domain.inventory.model.Inventory;
import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单领域服务
 * todo 只需要实现创建订单即可
 */
@Component
public class OrderDomainService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * 创建待付款订单
     */
    public Order createOrder(Order order) {
        Inventory inventory = inventoryRepository.getInventory(order.getSkuId());
        if (order.getAmount() > inventory.getSellableQuantity()) {
            throw new RuntimeException("库存不足");
        }
        order = orderRepository.createOrder(order);
        //看不懂入参，先把不为0的值作为需要修改的参数处理,正数加库存，负数减库存
        inventoryRepository.changeInventory(order.getSkuId(), -Long.valueOf(order.getAmount()), Long.valueOf(order.getAmount()), 0L);
        return order;
    }

    /**
     * 待付款订单支付成功
     */
    public void orderPaySuccess(CommandPay commandPay) {
        Order order = orderRepository.orderPaySuccess(commandPay);
        //看不懂入参，先把不为0的值作为需要修改的参数处理,正数加库存，负数减库存
        inventoryRepository.changeInventory(order.getSkuId(), 0L, -Long.valueOf(order.getAmount()), Long.valueOf(order.getAmount()));
    }

    /**
     * 待付款订单支付失败
     */
    public void orderPayFail(CommandPay commandPay) {
        Order order = orderRepository.orderPayFail(commandPay);
        //恢复库存？
        inventoryRepository.changeInventory(order.getSkuId(), Long.valueOf(order.getAmount()), -Long.valueOf(order.getAmount()), 0L);
    }
}
