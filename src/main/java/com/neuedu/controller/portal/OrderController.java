package com.neuedu.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Autowired
    IOrderService orderService;

    /**
     * 创建订单
     */
    @RequestMapping("create.do")
    public ServerResponse createOrder(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("需要登录");
        }

        return orderService.createOrder(userInfo.getId(),shippingId);
    }

    /**
     * 取消订单
     */
    @RequestMapping("cancel.do")
    public ServerResponse cancelOrder(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("需要登录");
        }

        return orderService.cancelOrder(userInfo.getId(),orderNo);
    }

    /**
     * 获取订单的商品信息(从购物车中)
     * 查询可能将要创建的订单
     */
    @RequestMapping("get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("需要登录");
        }

        return orderService.get_order_cart_product(userInfo.getId());
    }

    /**
     * 分页查询订单列表
     */
    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("需要登录");
        }

        return orderService.list(userInfo.getId(),pageNum,pageSize);
    }

    /**
     * 查询订单的详细信息
     */
    @RequestMapping("detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("需要登录");
        }

        return orderService.detail(orderNo);
    }

    /**
     * 预支付接口
     */
    @RequestMapping("pay.do/{orderNo}")
    public ServerResponse pay(HttpSession session,
                                   @PathVariable("orderNo") Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CUTTENTUSER);
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("需要登录");
        }

        return orderService.pay(userInfo.getId(),orderNo);
    }

    /**
     * 支付服务器回调商家服务器接口
     */
    @RequestMapping("callback.do")
    //因为返回给支付宝服务器，所以返回值为String
    public String callback(HttpServletRequest request){
        Map<String,String[]> callbackParams = request.getParameterMap();
        Map<String,String> signParam = Maps.newHashMap();
        Iterator<String> iterator = callbackParams.keySet().iterator();
        while(iterator.hasNext()){
            String key = iterator.next();
            String[] values = callbackParams.get(key);
            StringBuffer sbuffer=new StringBuffer();
            //字符串数组转字符串
            if(values!=null&&!values.equals("")) {
                for (int i=0;i<values.length;i++) {
                    if(i!=0){
                        sbuffer.append(",");
                    }
                    sbuffer.append(values[i]);
                }
            }

            signParam.put(key,sbuffer.toString());
        }
        System.out.println(signParam);



        //验证签名（保证接口是由支付宝调用的）
        try {
            signParam.remove("sign_type");//支付宝sdk中已有该参数，不移除会验签不通过
            boolean result = AlipaySignature.rsaCheckV2(signParam, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(result){
                //验签通过
                System.out.println("验签通过");
            }else{
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return orderService.callback(signParam);
    }

}
