package com.lbs.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbs.common.util.CookieUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.service.LoginService;

@Controller
public class LoginController {

	@Autowired
		private LoginService loginService;
		
	@RequestMapping("/page/login")
	public String toLoginPage(HttpServletRequest request,String redirect) {
		request.setAttribute("redirect", redirect);
		return "login";
	}
	
	
	
	//登录
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username,String password,HttpServletRequest request,HttpServletResponse response) {
		TaotaoResult result = loginService.loginin(username, password);
		if(result.getStatus()==200) {
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		}
		return result;
	}
	
	
	//退出登录
	@RequestMapping("/user/logout/{token}")
	@ResponseBody
	public Object loginout(@PathVariable String token,String callback) {
		TaotaoResult result = loginService.loginout(token);
		
		//判断是否使用jsonp
		if(StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue("result");
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		
		return result;
		
	}
}























