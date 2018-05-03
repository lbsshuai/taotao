package com.lbs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbs.common.pojo.EasyUITreeNode;
import com.lbs.common.util.TaotaoResult;
import com.lbs.mapper.TbContentCategoryMapper;
import com.lbs.pojo.TbContentCategory;
import com.lbs.pojo.TbContentCategoryExample;
import com.lbs.pojo.TbContentCategoryExample.Criteria;
import com.lbs.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	
	//内容分类管理操作
	@Override
	public List<EasyUITreeNode> findByParentId(Long id) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> nodes = new ArrayList<>();
		if(list!=null&&list.size()>0) {
			for(TbContentCategory tbContentCategory :list) {
				EasyUITreeNode node = new EasyUITreeNode();
				node.setId(tbContentCategory.getId());
				node.setText(tbContentCategory.getName());
				node.setState(tbContentCategory.getIsParent()?"closed":"open");
				nodes.add(node);
			}
		}
		return nodes;
	}
	
	//添加
	@Override
	public TaotaoResult insertNode(Long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//这段代码执行完,id直接返回到原对象
		contentCategoryMapper.insert(contentCategory);
		//判断父分类是不是父分类
		//得到父分类对象
		TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
		
		if(!parentCat.getIsParent()) {
			//不是父分类要改成父分类
			parentCat.setIsParent(true);
			parentCat.setUpdated(new Date());
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		
		return TaotaoResult.ok(contentCategory);
	
	}

	//修改
	@Override
	public TaotaoResult updateNode(Long id, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		return TaotaoResult.ok();
	}


	//删除
	@Override
	public TaotaoResult deleteNode(Long parentId, Long id) {
		
		contentCategoryMapper.deleteByPrimaryKey(id);
		TbContentCategoryExample example = new TbContentCategoryExample(); 
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		if(!(list!=null&&list.size()>0)) {
			TbContentCategory category = new TbContentCategory();
			category.setId(parentId);
			category.setIsParent(false);
			category.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKeySelective(category);
		}
		return TaotaoResult.ok();
	}
	
	
	

}













