package com.neuedu.controller.backend;


import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IProductService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    IProductService productService;


    /**
     * 新增或更新产品
     */
    @RequestMapping(value = "addOrUpdate.do")
    public ServerResponse addOrUpdate(HttpSession session, Product product){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        //判断用户权限
        if(userInfo.getRole()!= Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return productService.addOrUpdate(product);
    }

    /**
     * 产品上下架
     */
    @RequestMapping(value = "set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session,Product product){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        //判断用户权限
        if(userInfo.getRole()!= Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return productService.set_sale_status(product);
    }

    /**
     * 查看商品详情
     */
    @RequestMapping(value = "detail.do")
    public ServerResponse detail(HttpSession session,Integer productId){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        //判断用户权限
        if(userInfo.getRole()!= Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return productService.detail(productId);
    }

    /**
     * 查看商品列表  分页
     */
    @RequestMapping(value = "list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "0") Integer pageSize){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        //判断用户权限
        if(userInfo.getRole()!= Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return productService.list(pageNum,pageSize);
    }

    /**
     * 搜索商品  分页
     */
    @RequestMapping(value = "search.do")
    public ServerResponse search(HttpSession session,
                               @RequestParam(value = "productName",required = false)String productName,
                               @RequestParam(value = "productId",required = false)Integer productId,
                               @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "0") Integer pageSize){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        //判断用户权限
        if(userInfo.getRole()!= Const.RoleEnum.ROLE_ADMIN.getCode()){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }

        return productService.search(productId,productName,pageNum,pageSize);
    }

}
