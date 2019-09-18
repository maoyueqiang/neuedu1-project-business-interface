package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;

public interface IAddressService {
    /**
     * 添加收货地址
     */
    ServerResponse add(Integer userId,Shipping shipping);

    /**
     * 删除收货地址
     */
    ServerResponse del(Integer userId,Integer shippingId);

    /**
     * 更新收货地址
     */
    ServerResponse update(Shipping shipping);

    /**
     * 查看收货地址
     */
    ServerResponse select(Integer shippingId);

    /**
     * 查看收货地址列表  分页
     */
    ServerResponse selectAll(Integer pageNum,Integer pageSize);

    /**
     * 查看某人的收货地址列表  分页
     */
    ServerResponse selectAllByUserId(Integer userId,Integer pageNum,Integer pageSize);
}
