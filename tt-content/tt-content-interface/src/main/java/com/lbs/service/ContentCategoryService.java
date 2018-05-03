package com.lbs.service;

import java.util.List;

import com.lbs.common.pojo.EasyUITreeNode;
import com.lbs.common.util.TaotaoResult;

public interface ContentCategoryService {

	//显示列表
	List<EasyUITreeNode> findByParentId(Long id);
	
	//添加
	TaotaoResult insertNode(Long parentId ,String name);
	
	//修改
	TaotaoResult updateNode(Long id ,String name);
	
	//删除
	TaotaoResult deleteNode(Long parentId ,Long id);
}
