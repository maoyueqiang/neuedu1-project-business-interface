package com.neuedu.service.Impl;

import com.google.common.collect.Lists;
import com.neuedu.VO.CartProductVO;
import com.neuedu.VO.CartVO;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CartMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Cart;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICartService;
import com.neuedu.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    //向购物车中添加商品
    public ServerResponse add(Integer userId,Integer productId, Integer count) {
        //参数非空校验
        if(productId==null){
            return ServerResponse.createServerResponseByFail("商品id不能为空");
        }
        if(count==null){
            return ServerResponse.createServerResponseByFail("商品数量不能为空");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createServerResponseByFail("要添加的商品不存在");
        }

        //根据userId和productId查询购物信息
        Cart cart = cartMapper.selectCartByUseridAndProductid(userId,productId);
        if(cart==null){
            //添加
            Cart cart1=new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.insert(cart1);
        }
        else{
            //更新
            Cart cart1 = new Cart();
            cart1.setId(cart.getId());
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckedEnum.PRODUCT_CHECKED.getCode());
            cartMapper.updateByPrimaryKey(cart1);
        }

        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    @Override
    //显示购物车列表
    public ServerResponse list(Integer userId) {

        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    @Override
    //更新购物车中某商品的数量
    public ServerResponse update(Integer userId,Integer productId, Integer count) {
        //参数非空校验
        if (productId == null || count == null) {
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //根据userId和productId查询购物信息
        Cart cart = cartMapper.selectCartByUseridAndProductid(userId, productId);
        if (cart == null) {
            return ServerResponse.createServerResponseByFail("购物车中无该商品");
        }

        //更新
        cart.setQuantity(count);
        cartMapper.updateByPrimaryKey(cart);


        CartVO cartVO = getCartVOLimit(userId);
        return ServerResponse.createServerResponseBySuccess(cartVO);
    }

    @Override
    //移除购物车中某商品
    public ServerResponse delete(Integer userId, String productIds) {
        //非空校验
        if(productIds==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //string->int
        List<Integer> productIdList = Lists.newArrayList();
        String[] productIdsArr=productIds.split(",");
        if(productIdsArr.length>0&&productIdsArr!=null){
            for(String productIdstr:productIdsArr){
                Integer productId=Integer.parseInt(productIdstr);
                productIdList.add(productId);
            }
        }

        //删除商品
        cartMapper.deleteByUseridAndProductIds(userId,productIdList);

        //返回结果
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    //购物车选中或取消选中某商品/全部
    public ServerResponse select(Integer userId, Integer productId,Integer checked) {
        //dao接口
        cartMapper.selectOrUnselectProduct(userId,productId,checked);

        //返回结果
        return ServerResponse.createServerResponseBySuccess(getCartVOLimit(userId));
    }

    @Override
    //统计购物车中商品数量
    public ServerResponse get_cart_product_count(Integer userId) {
        int count=cartMapper.get_cart_product_count(userId);
        return ServerResponse.createServerResponseBySuccess(count);
    }


    private CartVO getCartVOLimit(Integer userId){
        CartVO cartVO = new CartVO();
        //根据userId查询购物信息
        List<Cart> cartList = cartMapper.selectCartByUserid(userId);

        //转换为用来显示的购物车列表
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        //购物车总价格
        BigDecimal totlalPrice=new BigDecimal(0);
        if(cartList!=null&&cartList.size()>0){
            for (Cart cart:cartList) {
                CartProductVO cartProductVO=new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductChecked(cart.getChecked());

                //查询商品信息赋值
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if(product!=null){
                    cartProductVO.setProductId(cart.getProductId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    int stock = product.getStock();
                    int limitProductCount = 0;
                    if(stock>=cart.getQuantity()){
                        limitProductCount=cart.getQuantity();
                        cartProductVO.setLimitQuantity("LINIT_NUM_SUCCESS");
                    }
                    else{//商品库存不足
                        limitProductCount=stock;
                        //更新购物车中商品数量
                        Cart cart1 = new Cart();
                        cart1.setId(cart.getId());
                        cart1.setProductId(cart.getProductId());
                        cart1.setUserId(userId);
                        cart1.setQuantity(stock);
                        cart1.setChecked(cart.getChecked());
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }
                    cartProductVO.setQuantity(limitProductCount);

                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVO.getQuantity())));

                }
                if(cartProductVO.getProductChecked()==Const.CartCheckedEnum.PRODUCT_CHECKED.getCode()) {
                    totlalPrice = BigDecimalUtils.add(totlalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartProductVOList(cartProductVOList);

        //购物车总价格
        cartVO.setCarttotalprice(totlalPrice);

        //判断购物车商品是否全选
        int count = cartMapper.isCheckedAll(userId);
        if(count>0){
            cartVO.setIsallchecked(false);
        }
        else{
            cartVO.setIsallchecked(true);
        }
        return cartVO;
    }
}
