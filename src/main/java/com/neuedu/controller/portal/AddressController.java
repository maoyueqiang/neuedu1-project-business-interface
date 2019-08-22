package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IAddressService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class AddressController {
    @Autowired
    IAddressService addressService;

    /**
     * 添加收货地址
     */
    @RequestMapping("add.do")
    public ServerResponse add(HttpSession session, Shipping shipping){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return addressService.add(userInfo.getId(),shipping);
    }

    /**
     * 删除收货地址
     */
    @RequestMapping("del.do")
    public ServerResponse del(HttpSession session, Integer shippingId){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return addressService.del(userInfo.getId(),shippingId);
    }

    /**
     * 更新收货地址
     */
    @RequestMapping("update.do")
    public ServerResponse update(HttpSession session, Shipping shipping){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        shipping.setUserId(userInfo.getId());
        return addressService.update(shipping);
    }

    /**
     * 查看收货地址
     */
    @RequestMapping("select.do")
    public ServerResponse select(HttpSession session, Integer shippingId){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return addressService.select(shippingId);
    }

    /**
     * 查看收货地址列表 分页
     */
    @RequestMapping("selectAll.do")
    public ServerResponse selectAll(HttpSession session,
                                    @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                    @RequestParam(required = false,defaultValue = "10")Integer pageSize){
        //判断用户是否登录
        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return addressService.selectAll(pageNum,pageSize);
    }


}
