package com.lbs.order.service;

import com.lbs.common.util.TaotaoResult;
import com.lbs.order.pojo.OrderInfo;

public interface OrderService {

	
	//生成订单
	TaotaoResult addOrder(OrderInfo orderInfo);
}
