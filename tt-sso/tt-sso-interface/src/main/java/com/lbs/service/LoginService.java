package com.lbs.service;

import com.lbs.common.util.TaotaoResult;

public interface LoginService {

	//登录
	TaotaoResult loginin(String username,String password);
	
	//退出
	TaotaoResult loginout(String token);
}
