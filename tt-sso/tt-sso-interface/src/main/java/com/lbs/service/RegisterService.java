package com.lbs.service;

import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbUser;

public interface RegisterService {

	//校验输入的username、phone、email是否符合要求
	TaotaoResult checkUser(String param,Integer type);
	
	//注册
	TaotaoResult addUser(TbUser user);
}
