package com.lbs.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbs.cart.service.RedisCartService;
import com.lbs.common.util.TaotaoResult;
import com.lbs.order.pojo.OrderInfo;
import com.lbs.order.service.OrderService;
import com.lbs.pojo.TbItem;
import com.lbs.pojo.TbUser;

@Controller
public class OrderController {

	@Autowired
	private RedisCartService redisCartService;
	
	@Autowired
	private OrderService orderService;

	@RequestMapping("/order/order-cart")
	public String showInfo(HttpServletRequest request) {
		// 当用户点击“去结算”请求的controller
		// 展示用户的收货地址信息：根据用户的id得到地址信息（用户必须登录，如果没有登录，强制登陆）
		// 展示支付方式：
		// 展示购物车中商品：从redis中取商品
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> list = redisCartService.getItemsFromRedis(user.getId());
		request.setAttribute("cartList", list);

		return "order-cart";
	}
	
	//生成订单
	@RequestMapping("/order/create")
	public String createOrder(HttpServletRequest request,OrderInfo orderInfo) {
		TbUser user = (TbUser) request.getAttribute("user");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//添加数据
		TaotaoResult result = orderService.addOrder(orderInfo);
		
		//订单生成后清空购物车
		if(result.getStatus()==200) {
			redisCartService.clearRedisCart(user.getId());
		}
		request.setAttribute("orderId", result.getData().toString());
		request.setAttribute("payment", orderInfo.getPayment());
		
		return "success";
	}
	
	
	
	
}






















