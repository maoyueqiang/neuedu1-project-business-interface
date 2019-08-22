package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    ICartService cartService;

    /**添加商品
     * 向购物车中添加商品
     * @param session
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping("add.do")
    public ServerResponse add(HttpSession session,Integer productId, Integer count){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.add(userInfo.getId(),productId,count);
    }

    /**
     * 显示购物车列表
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.list(userInfo.getId());
    }

    /**
     * 更新购物车中某个商品的数量
     * @return
     */
    @RequestMapping("update.do")
    public ServerResponse update(HttpSession session,Integer productId, Integer count){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.update(userInfo.getId(),productId,count);
    }


    /**
     * 移除购物车中某个商品
     * @return
     */
    @RequestMapping("delete_product.do")
    public ServerResponse delete_product(HttpSession session,String productIds){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.delete(userInfo.getId(),productIds);
    }

    /**
     * 购物车中选中某个商品
     * @return
     */
    @RequestMapping("select.do")
    public ServerResponse select(HttpSession session,Integer productId){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     * 购物车中取消选中某个商品
     * @return
     */
    @RequestMapping("unselect.do")
    public ServerResponse unselect(HttpSession session,Integer productId){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),productId,Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     * 购物车中全选
     * @return
     */
    @RequestMapping("select_all.do")
    public ServerResponse select_all(HttpSession session){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
    }

    /**
     * 购物车中取消全选
     * @return
     */
    @RequestMapping("unselect_all.do")
    public ServerResponse unselect_all(HttpSession session){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),null,Const.CartCheckedEnum.PRODUCT_UNCHECKED.getCode());
    }

    /**
     * 查询购物车中产品的数量
     */
    @RequestMapping("get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        return cartService.get_cart_product_count(userInfo.getId());
    }

}
