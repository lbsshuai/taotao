package com.lbs.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lbs.common.jedis.JedisClient;
import com.lbs.common.util.TaotaoResult;
import com.lbs.mapper.TbOrderItemMapper;
import com.lbs.mapper.TbOrderMapper;
import com.lbs.mapper.TbOrderShippingMapper;
import com.lbs.order.pojo.OrderInfo;
import com.lbs.order.service.OrderService;
import com.lbs.pojo.TbOrderItem;
import com.lbs.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ORDER_ID}")
	private String REDIS_ORDER_ID;
	
	@Value("${REDIS_ORDERID_START}")
	private String REDIS_ORDERID_START;
	
	@Value("${REDIS_ORDER_DETAIL}")
	private String REDIS_ORDER_DETAIL;
	
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	
	@Override
	public TaotaoResult addOrder(OrderInfo orderInfo) {
		//向订单表中添加订单
		//生成订单id,redis incr
		Boolean boo = jedisClient.exists(REDIS_ORDER_ID);
		if(!boo) {
			jedisClient.set(REDIS_ORDER_ID, REDIS_ORDERID_START);
		}
		String orderId = jedisClient.incr(REDIS_ORDER_ID).toString();
		
		orderInfo.setOrderId(orderId);
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		orderMapper.insert(orderInfo);
		
		//向订单商品表中添加数据
		List<TbOrderItem> items = orderInfo.getOrderItems();
		for(TbOrderItem orderItem : items) {
			String id = jedisClient.incr(REDIS_ORDER_DETAIL).toString();
			orderItem.setId(id);
			orderItem.setOrderId(orderId);
			orderItemMapper.insert(orderItem);
		}
		//向收货地址表中添加地址
		TbOrderShipping shopping = orderInfo.getOrderShipping();
		shopping.setOrderId(orderId);
		shopping.setCreated(new Date());
		shopping.setUpdated(new Date());
		orderShippingMapper.insert(shopping);
		
		return TaotaoResult.ok(orderId);//返回订单号
	}

}





















