package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;

public interface IProductService {

    /**
     * 后台-新增或更新产品
     */
    ServerResponse addOrUpdate(Product product);

    /**
     * 后台-产品上下架
     */
    ServerResponse set_sale_status(Product product);

    /**
     * 后台-查看商品详情
     */
    ServerResponse detail(Integer productId);

    /**
     * 后台-查看商品列表  分页
     */
    ServerResponse list(Integer pageNum,Integer pageSize);

    /**
     * 后台-搜索商品  分页
     */
    ServerResponse search(Integer productId,String productName,Integer pageNum,Integer pageSize);

    /**
     * 前台查看商品详情
     * @param productId
     * @return
     */
    ServerResponse detail_portal(Integer productId);

    /**
     * 前台搜索商品
     */
    ServerResponse list_portal(Integer categoryId, String keyword,
                        Integer pageNum,Integer pageSize,String orderBy);

    /**
     * 前台搜索轮播商品
     */
    ServerResponse bannerlist();

    /**
     * 前台搜索热卖商品
     */
    ServerResponse hotlist();

    /**
     * 前台搜索新品
     */
    ServerResponse newlist();
}
