package com.lbs.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.mapper.TbUserMapper;
import com.lbs.pojo.TbUser;
import com.lbs.pojo.TbUserExample;
import com.lbs.pojo.TbUserExample.Criteria;
import com.lbs.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	private TbUserMapper userMapper;
	
	/**
	 * 检查数据是否可用
	 * 第一个参数接收被检查的数据，第二个参数接收类型（1,2,3 用户名，电话，邮箱）
	 */
	@Override
	public TaotaoResult checkUser(String param, Integer type) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		if(type==1) {
			criteria.andUsernameEqualTo(param);
		}else if(type==2) {
			criteria.andPhoneEqualTo(param);
		}else if(type==3) {
			criteria.andEmailEqualTo(param);
		}else {
			return TaotaoResult.build(400, "数据类型不对");
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if(list==null||list.size()<=0) {
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

	/*
	 *注册功能
	 *1.判断数据是否为空
	 *2.判断数据是否可用
	 *3.补全数据
	 *4.MD5加密
	 */
	@Override
	public TaotaoResult addUser(TbUser user) {
		if(StringUtils.isBlank(user.getUsername())||StringUtils.isBlank(user.getPhone())){
			return TaotaoResult.build(400, "注册失败. 用户名或密码为空");
		}
		//判断数据是否可用
		TaotaoResult r = checkUser(user.getUsername(),1);
		if(!(boolean) r.getData()) {
			return TaotaoResult.build(400, "注册失败. 用户名已存在");
		}
		
		r = checkUser(user.getPhone(),2);
		if(!(boolean) r.getData()) {
			return TaotaoResult.build(400, "注册失败. 手机号错误");
		}
		user.setCreated(new Date());
		user.setUpdated(new Date());
		String pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(pass);
		
		userMapper.insert(user);
		
		return TaotaoResult.ok();
	}

}
















