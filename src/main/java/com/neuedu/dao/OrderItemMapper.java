package com.neuedu.dao;

import com.neuedu.pojo.OrderItem;
import java.util.List;

public interface OrderItemMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbggenerated
     */
    int insert(OrderItem record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbggenerated
     */
    OrderItem selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbggenerated
     */
    List<OrderItem> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order_item
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OrderItem record);

    /**
     * 批量插入订单明细
     */
    int insertBatch(List<OrderItem> orderItemList);

    /**
     * 根据订单号查询订单明细
     */
    List<OrderItem> findOrderItemsByOrderno(Long orderNo);
}