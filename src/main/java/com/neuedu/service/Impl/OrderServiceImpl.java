package com.neuedu.service.Impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.neuedu.VO.*;
import com.neuedu.alipay.Main;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.*;
import com.neuedu.pojo.*;
import com.neuedu.service.IOrderService;
import com.neuedu.utils.BigDecimalUtils;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.PropertiesUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService{

    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ShippingMapper shippingMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    PayInfoMapper payInfoMapper;


    @Value("${business.imageHost}")
    private String imageHost;

    /**
     * 创建订单
     */
    @Override
    public ServerResponse createOrder(Integer userId, Integer shippingId) {
        //step1:非空校验
        if(shippingId==null){
            return ServerResponse.createServerResponseByFail("地址参数不能为空");
        }

        //step2:根据userId查询购物车中已选择的商品-》List<Cart>
        List<Cart> cartList = cartMapper.findByUseridAndChecked(userId);

        //step3:List<Cart> -》 List<OrderItem>
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        //计算订单价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail("购物车中没有选中商品或购物车为空");
        }
        orderTotalPrice = getOrderPrice(orderItemList);
        //step4:创建订单order并将其保存至数据库
        Order order = createOrder(userId,shippingId,orderTotalPrice);
        if(order==null){
            return ServerResponse.createServerResponseByFail("订单创建失败");
        }

        //step5:将List<OrderItem>保存到数据库
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //批量插入
        orderItemMapper.insertBatch(orderItemList);

        //step6:扣商品库存
        reduceProductStock(orderItemList);

        //step7:清空已下单的商品
        cleanCart(cartList);

        //step8:返回OrderVO
        OrderVO orderVO = assmbleOrderVO(order,orderItemList,shippingId);

        return ServerResponse.createServerResponseBySuccess(orderVO);
    }

    /**
     * 取消订单
     */
    @Override
    public ServerResponse cancelOrder(Integer userId, Long orderNo) {
        //step1:非空校验
        if(orderNo==null||orderNo.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //step2:根据用户id和订单编号来查找订单
        Order order=orderMapper.findOrderByUseridAndOrderno(userId,orderNo);
        if(order==null){
            return ServerResponse.createServerResponseByFail("订单不存在");
        }

        //step3:判断订单状态
        if(order.getStatus()!=Const.OrderStatusEnum.ORDER_UN_PAY.getCode()){
            return ServerResponse.createServerResponseByFail("订单不可取消");
        }

        //step4:取消并返回结果
        order.setStatus(Const.OrderStatusEnum.ORDER_CANCELED.getCode());
        int count = orderMapper.updateByPrimaryKey(order);
        if(count>0){
            return ServerResponse.createServerResponseBySuccess("取消订单成功");
        }
        else{
            return ServerResponse.createServerResponseBySuccess("取消订单失败");
        }
    }

    /**
     * 获取订单的商品信息(从购物车中)
     */
    @Override
    public ServerResponse get_order_cart_product(Integer userId) {
        //step1:查询购物车
        List<Cart> cartList = cartMapper.findByUseridAndChecked(userId);

        //step2:List<Cart> -》 List<OrderItem>
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        //step3:组装vo
        CartOrderItemVO cartOrderItemVO = new CartOrderItemVO();
        cartOrderItemVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail("购物车为空");
        }
        for(OrderItem orderItem:orderItemList){
            orderItemVOList.add(assmbleOrderItemVO(orderItem));
        }
        cartOrderItemVO.setOrderItemVOList(orderItemVOList);
        cartOrderItemVO.setTotalPrice(getOrderPrice(orderItemList));

        //step4:返回结果
        return ServerResponse.createServerResponseBySuccess(cartOrderItemVO);
    }

    /**
     * 分页查询订单列表
     */
    @Override
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize) {

        //step1:判断用户权限
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        List<Order> orderList = Lists.newArrayList();
        Page page = PageHelper.startPage(pageNum,pageSize);
        if(userInfo.getRole()==Const.RoleEnum.ROLE_ADMIN.getCode()){
            //查询所有订单
            orderList = orderMapper.selectAll();
        }
        else{
            //查询自己的订单
            orderList = orderMapper.findOrderByUserid(userId);
        }
        //step2:查询订单列表
        if(orderList==null||orderList.size()==0){
            return  ServerResponse.createServerResponseByFail("未查询到订单");
        }

        //step3:转换为OrderVO
        List<OrderVO> orderVOList = Lists.newArrayList();
        for(Order order:orderList){
            List<OrderItem> orderItemList = orderItemMapper.findOrderItemsByOrderno(order.getOrderNo());
            OrderVO orderVO = assmbleOrderVO(order,orderItemList,order.getShippingId());
            orderVOList.add(orderVO);
        }

        //step3:返回结果
        PageInfo pageInfo = new PageInfo(page);
        pageInfo.setList(orderVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    /**
     * 查询订单的详细信息
     */
    @Override
    public ServerResponse detail(Long orderNo) {
        //非空校验
        if(orderNo==null||orderNo.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //查询订单
        Order order = orderMapper.findOrderByOrderno(orderNo);
        if(order==null||order.equals("")){
            return ServerResponse.createServerResponseByFail("订单不存在");
        }

        //查询OrderVO
        List<OrderItem> orderItemList = orderItemMapper.findOrderItemsByOrderno(orderNo);
        OrderVO orderVO = assmbleOrderVO(order,orderItemList,order.getShippingId());
        return ServerResponse.createServerResponseBySuccess(orderVO);
    }

    @Override
    public ServerResponse pay(Integer userId, Long orderNo) {
        //非空校验
        if(orderNo==null||orderNo.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //获取订单
        Order order = orderMapper.findOrderByUseridAndOrderno(userId,orderNo);
        if(order==null||order.equals("")){
            return ServerResponse.createServerResponseByFail("订单不存在");
        }

        //支付
        return pay(order);
    }

    /**
     * 支付服务器回调商家服务器接口
     */
    @Override
    public String callback(Map<String, String> requestParams) {
        //step1:获取参数信息
        String orderNo = requestParams.get("out_trade_no");
        String trade_no = requestParams.get("trade_no");
        String trade_status = requestParams.get("trade_status");
        String payment_time = requestParams.get("gmt_payment");

        //step2:根据订单号查询订单
        Order order = orderMapper.findOrderByOrderno(Long.parseLong(orderNo));
        if(order==null||order.equals("")){
            return "fail";
        }

        //支付成功
        //修改订单状态
        if(trade_status.equals("TRADE_SUCCESS")){
            Order order1 = new Order();
            order1.setOrderNo(Long.parseLong(orderNo));
            order1.setStatus(Const.OrderStatusEnum.ORDER_PAYED.getCode());
            order1.setPaymentTime(DateUtils.strToDate(payment_time));
            int count = orderMapper.updateOrderstatusAndPaymenttimeByOrderno(order1);
            if(count<=0){
                return "fail";
            }
        }

        //增加支付记录
        PayInfo payInfo = new PayInfo();
        payInfo.setOrderNo(Long.parseLong(orderNo));
        payInfo.setUserId(order.getUserId());
        payInfo.setPayPlatform(Const.PaymentTypeEnum.ONLINE.getCode());
        payInfo.setPlatformNumber(trade_no);
        payInfo.setPlatformStatus(trade_status);
        int count = payInfoMapper.insert(payInfo);
        if(count<=0){
            return "fail";
        }

        return "success";
    }

    /**
     *购物车列表转为订单详情列表
     */
    private ServerResponse getCartOrderItem(Integer userId, List<Cart> cartList){
        //非空校验
        if(cartList==null||cartList.size()==0){
            return ServerResponse.createServerResponseByFail("购物车为空");
        }

        //转换
        List<OrderItem> orderItemList = Lists.newArrayList();
        for(Cart cart:cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(product==null){
                return ServerResponse.createServerResponseByFail("id为"+cart.getProductId()+"的商品不存在");
            }
            if(product.getStatus()!= Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
                return ServerResponse.createServerResponseByFail("id为"+cart.getProductId()+"的商品已下架");
            }
            if(product.getStock()<cart.getQuantity()){
                return ServerResponse.createServerResponseByFail("id为"+cart.getProductId()+"的商品库存不足");
            }

            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            orderItem.setCurrentUnitPrice(product.getPrice());

            orderItem.setQuantity (cart.getQuantity());

            orderItemList.add(orderItem);
        }
        return ServerResponse.createServerResponseBySuccess(orderItemList);
    }

    /**
     * 创建订单
     */
    private Order createOrder(Integer userId, Integer shippingId, BigDecimal orderTotalPrice){
        Order order=new Order();
        order.setOrderNo(generateOrderNO());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setStatus(Const.OrderStatusEnum.ORDER_UN_PAY.getCode());
        //订单金额
        order.setPayment(orderTotalPrice);
        order.setPostage(0);//包邮
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE.getCode());

        //保存订单
        int count = orderMapper.insert(order);
        if(count>0){
            return order;
        }
        else{
            return null;
        }


    }

    /**
     * 生成订单编号
     */
    private Long generateOrderNO(){
        //根据当前时间戳  和   随机数  生成订单编号
        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    /**
     * 计算订单总价格
     */
    private BigDecimal getOrderPrice(List<OrderItem> orderItemList){
        BigDecimal bigDecimal = new BigDecimal("0");
        for(OrderItem orderItem:orderItemList){
            bigDecimal=BigDecimalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return bigDecimal;
    }

    /**
     * 扣库存
     */
    private void reduceProductStock(List<OrderItem> orderItemList){
        if(orderItemList!=null&&orderItemList.size()>0){
            for(OrderItem orderItem:orderItemList){
                Integer productId = orderItem.getProductId();
                Integer quantity = orderItem.getQuantity();
                Product product = productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock()-quantity);
                productMapper.updateByPrimaryKey(product);
            }
        }
    }

    /**
     * 清空购物车中已选中的商品
     */
    private void cleanCart(List<Cart> cartList){
        if(cartList!=null&&cartList.size()>0)
            cartMapper.batchDelete(cartList);
    }

    /**
     * Order、OrderItemList、Shipping转换为OrderVO
     */
    private OrderVO assmbleOrderVO(Order order,List<OrderItem> orderItemList,Integer shippingId){
        OrderVO orderVO=new OrderVO();
        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO = assmbleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping!=null){
            orderVO.setShippingId(shippingId);
            ShippingVO shippingVO = assmbleShippingVO(shipping);
            orderVO.setShippingVO(shippingVO);
            orderVO.setReceiverName(shipping.getReceiverName());
        }

        orderVO.setStatus(order.getStatus());
        Const.OrderStatusEnum orderStatusEnum = Const.OrderStatusEnum.codeOf(order.getStatus());
        if(orderStatusEnum!=null)
            orderVO.setStatusDesc(orderStatusEnum.getDesc());

        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());

        Const.PaymentTypeEnum paymentTypeEnum = Const.PaymentTypeEnum.codeOf(order.getPaymentType());
        if(paymentTypeEnum!=null){
            orderVO.setPaymentTypeDesc(paymentTypeEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());

        return orderVO;
    }

    /**
     * OrderItem转换为OrderItemVO
     */
    private OrderItemVO assmbleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        if(orderItem!=null){
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateTOStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        }
        return orderItemVO;
    }

    /**
     * shipping转换为shippingVO
     */
    private ShippingVO assmbleShippingVO(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();

        if(shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverProvince(shipping.getReceiverProvince());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
        return shippingVO;
    }

    private static Log log = LogFactory.getLog(Main.class);

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    // 支付调用
    public ServerResponse pay(Order order) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "【睿乐购】平台商城支付";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品共"+order.getPayment()+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "30m";

        //根据订单查询订单明细
        List<OrderItem> orderItemList = orderItemMapper.findOrderItemsByOrderno(order.getOrderNo());
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createServerResponseByFail("没有可购买的商品");
        }

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        for(OrderItem orderItem:orderItemList){
            // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(), orderItem.getCurrentUnitPrice().longValue(), orderItem.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress).setNotifyUrl("http://4aa6uq.natappfree.cc/order/callback.do")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                System.out.println(response);

                // 需要修改为运行机器上的路径
                String filePath = String.format("f:/qrcode/qr-%s.png",
                        response.getOutTradeNo());
                log.info("filePath:" + filePath);
                //引入谷歌的二维码
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);

                PayVO payVO = new PayVO(order.getOrderNo(),imageHost+"qr-"+response.getOutTradeNo()+".png");
                return ServerResponse.createServerResponseBySuccess(payVO);

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return ServerResponse.createServerResponseByFail();
    }
}
