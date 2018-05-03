package com.lbs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbs.mapper.TbItemMapper;
import com.lbs.pojo.TbItem;
import com.lbs.pojo.TbItemExample;
import com.lbs.pojo.TbItemExample.Criteria;
import com.lbs.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	 
	@Override
	public TbItem findById(Long id) {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		
		return list.get(0);
	}

	
}
