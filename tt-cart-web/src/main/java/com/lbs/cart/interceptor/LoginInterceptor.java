package com.lbs.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lbs.common.util.CookieUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbUser;
import com.lbs.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		if(StringUtils.isBlank(token)) {
			//当获取不到token时,证明用户没有登录
			return true;//放行
		}
		//根据token获取用户信息
		TaotaoResult result = tokenService.findUserInfoByToken(token);
		
		//判断token是否过期
		if(result.getStatus()!=200) {
			//用户登录过期
			return true; //放行
		}
		//用户已登录
		//获取用户信息
		TbUser user  = (TbUser) result.getData();
		request.setAttribute("user", user);
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
