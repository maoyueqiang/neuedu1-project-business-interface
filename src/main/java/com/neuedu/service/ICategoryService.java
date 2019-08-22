package com.neuedu.service;

import com.neuedu.common.ServerResponse;

public interface ICategoryService {

    /**
     * 获取品类的子节点
     */
    ServerResponse get_category(Integer categoryId);

    /**
     * 获取当前分类id及递归子节点categoryId
     */
    ServerResponse get_deep_category(Integer categoryId);
}
