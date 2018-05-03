package com.lbs.service.impl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.lbs.common.jedis.JedisClient;
import com.lbs.common.util.JsonUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.mapper.TbUserMapper;
import com.lbs.pojo.TbUser;
import com.lbs.pojo.TbUserExample;
import com.lbs.pojo.TbUserExample.Criteria;
import com.lbs.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {


	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_SESSION_EXPIRE}")
	private int REDIS_SESSION_EXPIRE;
	
	/*
	 * 登录
	 * 注意:
	 * 1.首先确定用户名是否存在
	 * 2.检验密码是否正确
	 * 3.获取token,以它为key值,存入Redis缓存中,并设置过期时间,
	 * 存入用户信息时,要把密码设置为null, 避免泄露信息
	 */
	@Override
	public TaotaoResult loginin(String username, String password) {
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)) {
			return TaotaoResult.build(400, "用户名或密码为空");
		}
		//判断用户名是否存在
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		
		List<TbUser> list = userMapper.selectByExample(example);
		if(list==null||list.size()<=0) {
			return TaotaoResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		
		//判断密码是否正确
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或密码错误");
		} 
		//获取token,存入Redis
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		//将登录信息存入Redis,并设置其过期时间
		jedisClient.set("SESSION"+token, JsonUtils.objectToJson(user));
		jedisClient.expire("SESSION"+token, REDIS_SESSION_EXPIRE);
		
		return TaotaoResult.ok(token);//返回token
	}

	
	/**
	 * 用户退出登录
	 * Redis中的token失效
	 * 
	 */
	@Override
	public TaotaoResult loginout(String token) {
		
		jedisClient.del("SESSION"+token);
		
		return TaotaoResult.ok();
	}

	
}





















