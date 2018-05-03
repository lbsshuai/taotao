package com.lbs.service;

import com.lbs.common.util.TaotaoResult;

public interface TokenService {

	//通过token在Redis中查询用户信息
	TaotaoResult findUserInfoByToken(String token);
}
