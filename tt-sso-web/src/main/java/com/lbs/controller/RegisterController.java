package com.lbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbUser;
import com.lbs.service.RegisterService;

@Controller
public class RegisterController {

	@Autowired
	private RegisterService registerService;
	

	@RequestMapping("/page/register")
	public String toRegisterPage()
	{
		return "register";
	}
	
	
	
	/*
	 * 未注册信息提供验证
	 * 检查数据是否可用
	 * 第一个参数接收被检查的数据，第二个参数接收类型（1,2,3 用户名，电话，邮箱）
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUser(@PathVariable String param,@PathVariable Integer type) {
		
		TaotaoResult result = registerService.checkUser(param, type);
		return result;
	}
	
	//注册功能
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult addUser(TbUser user) {
		return registerService.addUser(user);
	}
	
}















