package com.neuedu.controller.portal;


import com.neuedu.common.ServerResponse;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/category/")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;
    @Autowired
    IUserService userService;

    /**
     * 获取品类的子节点
     */
    @RequestMapping(value = "get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryId){

        return categoryService.get_category(categoryId);

    }

    /**
     * 获取当前分类id及递归子节点categoryId
     *
     */
    @RequestMapping(value = "get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session,Integer categoryId){


        return categoryService.get_deep_category(categoryId);

    }


}
