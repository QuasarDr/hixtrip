package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.sample.vo.OrderVO;

/**
 * 订单的service层
 */
public interface OrderService {

    OrderVO order(CommandOderCreateDTO commandOderCreateDTO);
}
