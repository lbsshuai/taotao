package com.lbs.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lbs.cart.service.RedisCartService;
import com.lbs.common.jedis.JedisClient;
import com.lbs.common.util.JsonUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.mapper.TbItemMapper;
import com.lbs.pojo.TbItem;

@Service
public class RedisCartServiceImpl implements RedisCartService {

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_CART_KEY}")
	private String REDIS_CART_KEY;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	//将商品添加到购物车
	@Override
	public TaotaoResult addItemTOCart(Long userId, Long itemId, Integer itemNum) {
		
		//判断redis中有没有这个商品
		Boolean flag = jedisClient.hexists(REDIS_CART_KEY+":"+userId, itemId+"");
		
		if(flag) {
			//存在该商品
			String json = jedisClient.hget(REDIS_CART_KEY+":"+userId, itemId+"");
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+itemNum);
			jedisClient.hset(REDIS_CART_KEY+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		}else {
			//不存在该商品
			//查询该商品的信息
			TbItem item = itemMapper.selectByPrimaryKey(itemId);
			item.setNum(itemNum);
			String image = item.getImage();
			if(StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}
			jedisClient.hset(REDIS_CART_KEY+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		}
		
		return TaotaoResult.ok();

	}

	/*
	 * 将Cookie中的商品加入到Redis缓存中
	 */
	@Override
	public TaotaoResult mergeToRedis(Long userId, List<TbItem> carts) {
		for(TbItem item : carts) {
			addItemTOCart(userId,item.getId(),item.getNum());
		}
		return TaotaoResult.ok();
	}

	/*
	 * 展示购物车中的商品
	 */
	@Override
	public List<TbItem> getItemsFromRedis(Long userId) {
		List<String> json = jedisClient.hvals(REDIS_CART_KEY+":"+userId);
		List<TbItem> carts = new ArrayList<TbItem>();
		for(String ss : json) {
			TbItem item = JsonUtils.jsonToPojo(ss, TbItem.class);
			
			carts.add(item);
		}
		return carts;
	}

	/*
	 * 修改redis中购物车中商品数量
	 */
	@Override
	public TaotaoResult updateItemNum(Long userId, Long itemId, Integer num) {
		String json = jedisClient.hget(REDIS_CART_KEY+":"+userId, itemId+"");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		jedisClient.hset(REDIS_CART_KEY+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		
		return TaotaoResult.ok();
	}

	/*
	 *删除redis中购物车中的商品
	 */
	@Override
	public TaotaoResult deleteItme(Long userId, Long itemId) {
		jedisClient.hdel(REDIS_CART_KEY+":"+userId, itemId+"");
		return TaotaoResult.ok();
	}
	
	
	/*
	 * 清空购物车
	 */
	@Override
	public TaotaoResult clearRedisCart(Long userId) {
		
		jedisClient.del(REDIS_CART_KEY+":"+userId);
		
		return null;
	}
	
	
	
	
}









