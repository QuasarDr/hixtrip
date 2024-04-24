package com.hixtrip.sample.entry;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.strategy.PaymentCallbackProcessor;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.sample.vo.OrderVO;
import com.hixtrip.sample.client.sample.vo.PayResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class OrderController {


    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentCallbackProcessor paymentCallbackProcessor;

    /**
     * @param commandOderCreateDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/create")
    public OrderVO order(@RequestBody CommandOderCreateDTO commandOderCreateDTO) {
        //登录信息可以在这里模拟
        var userId = "";
        commandOderCreateDTO.setUserId(userId);
        return orderService.order(commandOderCreateDTO);
    }

    /**
     * 【中、高级要求】需要使用策略模式处理至少三种场景：支付成功、支付失败、重复支付(自行设计回调报文进行重复判定)
     *
     * @param commandPayDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/pay/callback")
    public PayResultVO payCallback(@RequestBody CommandPayDTO commandPayDTO) {
        paymentCallbackProcessor.processCallback(commandPayDTO);
        //不知道返回什么，没有异常就是成功，需要一个全局异常捕捉类
        return new PayResultVO("200", "success");
    }

}
