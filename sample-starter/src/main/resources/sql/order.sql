CREATE TABLE `order` (
                         `id` varchar(255) NOT NULL COMMENT '订单号',
                         `user_id` varchar(255) NOT NULL COMMENT '购买人',
                         `sku_id` varchar(255) NOT NULL COMMENT 'SkuId',
                         `amount` int(11) NOT NULL COMMENT '购买数量',
                         `money` decimal(10,2) NOT NULL COMMENT '购买金额',
                         `pay_time` datetime NOT NULL COMMENT '购买时间',
                         `pay_status` varchar(255) NOT NULL COMMENT '支付状态',
                         `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志（0代表存在，1代表删除）',
                         `create_by` varchar(255) NOT NULL COMMENT '创建人',
                         `create_time` datetime NOT NULL COMMENT '创建时间',
                         `update_by` varchar(255) NOT NULL COMMENT '修改人',
                         `update_time` datetime NOT NULL COMMENT '修改时间',
                         PRIMARY KEY (`id`),
                         INDEX `idx_user_id` (`user_id`),
                         INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

# 分库键：选择user_id作为分库键。这样可以确保同一买家的所有订单都存储在同一数据库中，便于买家查询时仅在一个数据库中进行搜索，提高查询效率。
# 分表键：根据时间范围（如按月或季度）进行分表，使用create_time作为分表键。
# 买家查询使用userId作为索引可以加快查询
# 卖家可以用创建时间作为索引，提高查询效率，也可以通过定时任务统计订单数据作为缓存或者存入新表，卖家查询时单独从次数据中查询降低延迟。
# 数据库可以根据mysql的binlog考虑导入elasticsearch，通过elasticsearch查询数据满足客服的查询需求。卖家买家查询如有需求也可以全部从elasticsearch走。
# 排行榜榜单功能可以根据数据量，使用elasticsearch做聚合查询，或者使用大数据常用的如hadoop等