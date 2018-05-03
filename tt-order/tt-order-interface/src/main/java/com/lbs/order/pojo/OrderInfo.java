package com.lbs.order.pojo;

import java.util.List;

import com.lbs.pojo.TbOrder;
import com.lbs.pojo.TbOrderItem;
import com.lbs.pojo.TbOrderShipping;

public class OrderInfo extends TbOrder{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;
	
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

	
}
