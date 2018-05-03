package com.lbs.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbs.cart.service.RedisCartService;
import com.lbs.common.jedis.JedisClient;
import com.lbs.common.pojo.SearchItem;
import com.lbs.common.util.CookieUtils;
import com.lbs.common.util.JsonUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbItem;
import com.lbs.pojo.TbUser;
import com.lbs.service.ItemService;

@Controller
public class CartController {

	@Autowired 
	private ItemService itemService;
	
	@Value("${CART_COOKIE_TIME}")
	private Integer CART_COOKIE_TIME;
	
	@Autowired
	private RedisCartService redisCatService;
	
	/*
	 * 添加到购物车
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addItmeToCart(HttpServletRequest request,HttpServletResponse response,
				@PathVariable Long itemId ,@RequestParam(defaultValue="1")Integer num) {
		//判断是否登录,拦截器
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			//登录状态下
			System.out.println("登录状态下存入redis");
			redisCatService.addItemTOCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		
		//未登录情况下：购物车在cookie中，
		//得到cookie中的购物车："cart":JsonUtils.objectToJson(List<TbItem>)
		List<TbItem> carts = getCartFromCookie(request);
		
		//判断购物车中是否包含要添加的商品
		//如果包含,就直接修改数量
		//不包含就查找商品数据,添加到购物车中
		boolean flag = false;
		for(TbItem item : carts) {
			if(item.getId()==itemId.longValue()) {
				item.setNum(item.getNum()+num);
				flag = true;
				break;
			}
		}
		//如果flag仍为false时,证明购物车中没有该商品
		if(!flag) {
			TbItem item = itemService.findById(itemId);
			item.setNum(num);
			String image = item.getImage().split(",")[0];
			item.setImage(image);
			//将商品信息加入购物车
			carts.add(item);
		}
		
		//将购物车放入Cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(carts), CART_COOKIE_TIME, true);
		
		return "cartSuccess";
	}
	
	
	/*
	 * 得到Cookie中的购物车
	 */
	public List<TbItem> getCartFromCookie(HttpServletRequest request) {
		//从Cookie中取车
		String json = CookieUtils.getCookieValue(request, "cart",true );
		
		//当没有取到车
		if(StringUtils.isBlank(json)) {
			//返回一个空车
			List<TbItem> carts = new ArrayList<TbItem>();
			return carts;
		}
		//当取到车了,将json转化为车
		List<TbItem> list = JsonUtils.jsonToList(json,TbItem.class);
		return list;
	}
	
	
	/*
	 * 展示购物车中商品
	 */
	@RequestMapping("/cart/cart")
	public String showCartItems(HttpServletRequest request,HttpServletResponse response) {
		//判断是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		List<TbItem> carts = getCartFromCookie(request);
		
		/*List<SearchItem> carts1 = new ArrayList<SearchItem>();
		for(TbItem item : carts) {
			SearchItem searchItem = new SearchItem();
			searchItem.setCategory_name(item.getcat);
		}*/
		if(user!=null) {
			//在登录情况下
			//得到Cookie中的购物车
			redisCatService.mergeToRedis(user.getId(), carts);
			CookieUtils.deleteCookie(request, response, "cart");
			carts = redisCatService.getItemsFromRedis(user.getId());
		}
		
		request.setAttribute("cartList", carts);
		return "cart";
	} 
	
	/*
	 * 增加或减少商品数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{itemNum}")
	public TaotaoResult updateCartNumber(HttpServletRequest request,HttpServletResponse response,@PathVariable Long itemId,@PathVariable Integer itemNum){
		//用户登录情况下
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			TaotaoResult result = redisCatService.updateItemNum(user.getId(), itemId, itemNum);
			return result;
		}
		
		List<TbItem> carts = getCartFromCookie(request);
		for(TbItem item : carts) {
			if(item.getId()==itemId.longValue()) {
				item.setNum(itemNum);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(carts),CART_COOKIE_TIME,true);
		return TaotaoResult.ok();
		
	}
	
	
	/*
	 * 删除购物车中的商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCarItem(HttpServletRequest request,HttpServletResponse response,@PathVariable Long itemId) {
		//用户登录情况下
		TbUser user = (TbUser) request.getAttribute("user");
		if(user!=null) {
			redisCatService.deleteItme(user.getId(), itemId);
		}
		
		List<TbItem> carts = getCartFromCookie(request);
		for(TbItem item : carts) {
			if(item.getId()==itemId.longValue()) {
				carts.remove(item);
			}
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(carts),CART_COOKIE_TIME,true);
		return "redirect:/cart/cart.html";
	}
	
}













