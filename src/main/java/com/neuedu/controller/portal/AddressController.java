package com.neuedu.controller.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IAddressService;
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

        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);


        return addressService.add(userInfo.getId(),shipping);
    }

    /**
     * 删除收货地址
     */
    @RequestMapping("del.do")
    public ServerResponse del(HttpSession session, Integer shippingId){

        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);


        return addressService.del(userInfo.getId(),shippingId);
    }

    /**
     * 更新收货地址
     */
    @RequestMapping("update.do")
    public ServerResponse update(HttpSession session, Shipping shipping){

        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);


        shipping.setUserId(userInfo.getId());
        return addressService.update(shipping);
    }

    /**
     * 查看收货地址
     */
    @RequestMapping("select.do")
    public ServerResponse select(HttpSession session, Integer shippingId){

        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);


        return addressService.select(shippingId);
    }

    /**
     * 查看收货地址列表 分页
     */
    @RequestMapping("selectAll.do")
    public ServerResponse selectAll(HttpSession session,
                                    @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                    @RequestParam(required = false,defaultValue = "10")Integer pageSize){

        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);


        return addressService.selectAll(pageNum,pageSize);
    }

    /**
     * 查看某人的收货地址列表 分页
     */
    @RequestMapping("selectAllByUserId.do")
    public ServerResponse selectAllByUserId(HttpSession session,
                                    @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                    @RequestParam(required = false,defaultValue = "10")Integer pageSize){

        UserInfo userInfo =(UserInfo) session.getAttribute(Const.CUTTENTUSER);


        return addressService.selectAllByUserId(userInfo.getId(),pageNum,pageSize);
    }


}
