package com.lbs.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbs.common.util.JsonUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.service.TokenService;

@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
	//通过token获取用户信息
/*	@RequestMapping(value="user/token/{token}",produces=MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8")
	@ResponseBody
	public String selectByToken(@PathVariable String token,String callback) {
		TaotaoResult result = tokenService.findUserInfoByToken(token);
		
		//判断是否使用了jsonp
		if(StringUtils.isNotBlank(callback)){
			//返回js代码
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		
		return JsonUtils.objectToJson(result);
	
	}*/
	
	//通过token获取用户信息
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object selectByToken(@PathVariable String token,String callback) {
	
		TaotaoResult result = tokenService.findUserInfoByToken(token);
		//判断是否使用了jsonp
		if(StringUtils.isNotBlank(callback)) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
 	}
}

















