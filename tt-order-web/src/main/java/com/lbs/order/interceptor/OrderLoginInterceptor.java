package com.lbs.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lbs.cart.service.RedisCartService;
import com.lbs.common.util.CookieUtils;
import com.lbs.common.util.JsonUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbItem;
import com.lbs.pojo.TbUser;
import com.lbs.service.TokenService;

//判断用户是否登陆的拦截器，如果没有登录强制登陆
public class OrderLoginInterceptor implements HandlerInterceptor {

	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private RedisCartService redisCartService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		if(StringUtils.isBlank(token)) {
			//强制登录
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURI());
			return false;
		}
		//根据token获取用户信息
		TaotaoResult result = tokenService.findUserInfoByToken(token);
		if(result.getStatus()!=200) {
			//token过期,强制登录
			response.sendRedirect(SSO_URL+"/page/login?redirect="+request.getRequestURI());
			return false;
		}
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
			
		//当用户点击去下单时,如果还没登录
		//需要判断Cookie中是否有商品,有的话需要整合到redis购物车中
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if(StringUtils.isNotBlank(json)) {
			redisCartService.mergeToRedis(user.getId(), JsonUtils.jsonToList(json,TbItem.class));
		}
		return true;
	}

	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
