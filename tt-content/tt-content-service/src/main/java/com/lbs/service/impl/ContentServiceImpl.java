package com.lbs.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.lbs.common.jedis.JedisClient;
import com.lbs.common.pojo.EasyUIDataGridResult;
import com.lbs.common.util.JsonUtils;
import com.lbs.common.util.TaotaoResult;
import com.lbs.mapper.TbContentMapper;
import com.lbs.pojo.TbContent;
import com.lbs.pojo.TbContentExample;
import com.lbs.pojo.TbContentExample.Criteria;
import com.lbs.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper tbContentMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${CONTENT_REDIS_KEY}")
	private String CONTENT_REDIS_KEY;

	@Override
	public EasyUIDataGridResult selectList(Integer page, Integer rows, Long categoryId) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		PageHelper.startPage(page, rows);
		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);

		EasyUIDataGridResult easy = new EasyUIDataGridResult();
		easy.setRows(list);

		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		easy.setTotal(total);

		return easy;
	}

	// 内容保存
	@Override
	public TaotaoResult saveContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		int selective = tbContentMapper.insert(content);
		try {
			jedisClient.hset(CONTENT_REDIS_KEY, content.getCategoryId() + "", JsonUtils.objectToJson(content));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return TaotaoResult.ok();
	}

	// 门户页大广告显示
	@Override
	public List<TbContent> selectTbcontent(long categoryId) {
		/*
		 * TbContentExample example = new TbContentExample(); Criteria criteria =
		 * example.createCriteria(); criteria.andCategoryIdEqualTo(CONTENT_AD);
		 * List<TbContent> list = contentMapper.selectByExample(example);
		 */
		try {
			// 从redis缓存中查找数据
			String json = jedisClient.hget(CONTENT_REDIS_KEY, categoryId + "");
			if (StringUtils.isNotEmpty(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				System.out.println("我使用缓存存数据了");
				return list;
			}

			List<TbContent> list = tbContentMapper.selectByCat(categoryId);

			jedisClient.hset("CONTENT_REDIS_KEY", categoryId + "", JsonUtils.objectToJson(list));

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	// 删除内容
	@Override
	public TaotaoResult deleteContent(long[] ids) {
		for (long id : ids) {
			tbContentMapper.deleteByPrimaryKey(id);
		}

		return TaotaoResult.ok();
	}

	// 修改内容
	@Override
	public TaotaoResult editContent(TbContent content) {
		tbContentMapper.updateByPrimaryKeySelective(content);
		return TaotaoResult.ok();
	}

}
