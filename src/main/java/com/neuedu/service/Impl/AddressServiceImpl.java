package com.neuedu.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService{

    @Autowired
    ShippingMapper shippingMapper;

    @Override
    //添加收货地址
    public ServerResponse add(Integer userId, Shipping shipping) {
        //非空校验
        if(shipping!=null||!shipping.equals("")){
            //添加
            shipping.setUserId(userId);
            shippingMapper.insert(shipping);

            //返回结果
            Map<String,Integer> map= Maps.newHashMap();
            map.put("shippingId",shipping.getId());
            return ServerResponse.createServerResponseBySuccess(map);

        }
        return ServerResponse.createServerResponseByFail("参数不能为空");


    }

    @Override
    //删除收货地址
    public ServerResponse del(Integer userId, Integer shippingId) {
        //非空校验
        if(shippingId==null||shippingId.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //删除
        int count = shippingMapper.deleteByUseridAndShippingid(userId,shippingId);

        //返回结果
        if(count>0){
            return ServerResponse.createServerResponseBySuccess("删除成功");
        }
        else{
            return ServerResponse.createServerResponseBySuccess("删除失败");

        }

    }

    @Override
    //更新收货地址
    public ServerResponse update(Shipping shipping) {
        //非空校验
        if(shipping==null||shipping.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //更新
        int count = shippingMapper.updateBySelectiveKey(shipping);
        if(count>0){
            return ServerResponse.createServerResponseBySuccess("更新成功");
        }
        else{
            return ServerResponse.createServerResponseBySuccess("更新失败");
        }
    }

    @Override
    //查看收货地址
    public ServerResponse select(Integer shippingId) {
        //非空校验
        if(shippingId==null||shippingId.equals("")){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //查看地址
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping!=null&&!shipping.equals("")){
            return ServerResponse.createServerResponseBySuccess(shipping);
        }
        else
            return ServerResponse.createServerResponseByFail("地址不存在");
    }

    @Override
    //查看收货地址列表  分页
    public ServerResponse selectAll(Integer pageNum,Integer pageSize) {
        Page page= PageHelper.startPage(pageNum,pageSize);
        //查看地址列表
        List<Shipping> shippingList = shippingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(page);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }
}
