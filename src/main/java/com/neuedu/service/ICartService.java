package com.neuedu.service;


import com.neuedu.common.ServerResponse;

public interface ICartService {

    //向购物车中添加商品
    public ServerResponse add(Integer userId,Integer productId,Integer count);

    //显示购物车列表
    public  ServerResponse list(Integer userId);

    //更新购物车中某商品的数量
    public ServerResponse update(Integer userId,Integer productId,Integer count);

    //移除购物车中某商品
    public ServerResponse delete(Integer userId,String productIds);

    //购物车中选中或取消选中某商品
    public ServerResponse select(Integer userId,Integer productId,Integer checked);

    //查询购物车中产品的数量
    public ServerResponse get_cart_product_count(Integer userId);

}
