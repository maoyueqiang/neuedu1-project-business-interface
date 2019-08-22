package com.neuedu.service;

import com.neuedu.common.ServerResponse;

import java.util.Map;

public interface IOrderService {

    /**
     * 创建订单
     */
    ServerResponse createOrder(Integer userId,Integer shippingId);

    /**
     * 取消订单
     */
    ServerResponse cancelOrder(Integer userId,Long orderNo);

    /**
     * 获取订单的商品信息(从购物车中)
     */
    ServerResponse get_order_cart_product(Integer userId);

    /**
     * 分页查询订单列表
     */
    ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);

    /**
     * 查询订单的详细信息
     */
    ServerResponse detail(Long orderNo);

    /**
     * 预支付接口
     */
    ServerResponse pay(Integer userId,Long orderNo);

    /**
     * 支付回调接口
     */
    String callback(Map<String,String> requestParams);
}
