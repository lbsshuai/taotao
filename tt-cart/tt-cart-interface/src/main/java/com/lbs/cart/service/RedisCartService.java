package com.lbs.cart.service;

import java.util.List;

import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbItem;

public interface RedisCartService {

	//将商品添加到购物车
	TaotaoResult addItemTOCart(Long userId,Long itemId,Integer itemNum);
	
	//将Cookie中的商品加到Redis中
	TaotaoResult mergeToRedis(Long userId,List<TbItem> carts);
	
	//展现购物车中的商品
	 List<TbItem> getItemsFromRedis(Long userId);
	 
	//修改redis中购物车中商品数量
	TaotaoResult updateItemNum(Long userId,Long itemId,Integer num);
	 
	//删除redis中购物车中的商品
	TaotaoResult deleteItme(Long userId,Long itemId);
	
	//清空购物车
	TaotaoResult clearRedisCart(Long userId);
}



















