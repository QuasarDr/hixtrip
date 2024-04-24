package com.hixtrip.sample.app.strategy;

import com.hixtrip.sample.app.enums.PayStatusEnum;
import com.hixtrip.sample.app.factory.PayStrategyFactory;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.client.sample.vo.PayResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PaymentCallbackProcessor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PayStrategyFactory payStrategyFactory;

    public void processCallback(CommandPayDTO commandPayDTO) {
        //简单用redis处理判断是否处理过回调，如果已处理过，直接返回，没有可以用来做判断的字段，先拿订单id做幂等
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(commandPayDTO.getOrderId(), System.currentTimeMillis());
        if (Boolean.FALSE.equals(absent)) {
            log.info("回调已处理，orderId:" + commandPayDTO.getOrderId());
        }

        Optional<PayStatusEnum> payStatusEnumOptional = Arrays.stream(PayStatusEnum.class.getEnumConstants())
                .filter((e) -> e.getStatus().equals(commandPayDTO.getPayStatus())).findAny();

        if (payStatusEnumOptional.isEmpty()) {
            log.error("无具体支付策略！");
            throw new RuntimeException("无具体支付策略");
        }
        PaymentCallbackStrategy payStrategy = payStrategyFactory.getPayStrategy(payStatusEnumOptional.get());
        payStrategy.handle(commandPayDTO);
    }
}
