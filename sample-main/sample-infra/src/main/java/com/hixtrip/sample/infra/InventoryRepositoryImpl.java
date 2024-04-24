package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.model.Inventory;
import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Inventory getInventory(String skuId) {
        return (Inventory) redisTemplate.opsForValue().get("inventory:skuId:" + skuId);
    }

    /**
     * 修改库存
     *
     * @param skuId
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return
     */
    @Override
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        //不太了解库存业务，不清楚入参的作用
        String lockKey = "changeInventory:" + skuId;
        //可以使用redis的INCR 、decr等命令完成无锁秒杀，利用原子性操作一次性完成库存的查询和减少来防止并发产生的冲突
        //先尝试加锁，如果加锁失败，则等待。不太清楚这块，先一直尝试直到获取到锁
        while (true) {
            Boolean absent = redisTemplate.opsForValue().setIfAbsent(lockKey, System.currentTimeMillis());
            if (Boolean.TRUE.equals(absent)) {
                break;
            }
        }
        try {
            Inventory inventory = getInventory(skuId);
            if (inventory == null) {
                return false;
            }
            if (sellableQuantity != 0L) {
                Long inventorySellableQuantity = inventory.getSellableQuantity();
                if (inventorySellableQuantity + sellableQuantity < 0L) {
                    throw new RuntimeException("可售库存");
                }
                inventory.setSellableQuantity(inventorySellableQuantity + sellableQuantity);
            }
            if (withholdingQuantity != 0L) {
                Long inventoryWithholdingQuantity = inventory.getWithholdingQuantity();
                if (withholdingQuantity + inventoryWithholdingQuantity < 0L) {
                    throw new RuntimeException("预占库存不足");
                }
                inventory.setWithholdingQuantity(withholdingQuantity + inventoryWithholdingQuantity);
            }
            if (occupiedQuantity != 0L) {
                Long inventoryOccupiedQuantity = inventory.getOccupiedQuantity();
                if (occupiedQuantity + inventoryOccupiedQuantity < 0L) {
                    throw new RuntimeException("预占库存不足");
                }
                inventory.setOccupiedQuantity(occupiedQuantity + inventoryOccupiedQuantity);
            }

            redisTemplate.opsForValue().set("inventory:skuId:" + skuId, inventory);
        } finally {
            redisTemplate.delete(lockKey);
        }
        return false;
    }
}
