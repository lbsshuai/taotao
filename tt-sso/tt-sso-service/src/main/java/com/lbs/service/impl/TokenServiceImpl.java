package com.lbs.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lbs.common.jedis.JedisClient;
import com.lbs.common.util.JsonUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbUser;
import com.lbs.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_SESSION_EXPIRE}")
	private Integer REDIS_SESSION_EXPIRE;
	
	
	/**
	 * 在门户页上面显示用户名
	 * 通过token在Redis缓存中获取
	 */
	@Override
	public TaotaoResult findUserInfoByToken(String token) {
		String json = jedisClient.get("SESSION"+token);
		if(StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "用户已经过期,请重新登录");
		}
		
		jedisClient.expire("SESSION"+token, REDIS_SESSION_EXPIRE);
		
		return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
	}

	
}














