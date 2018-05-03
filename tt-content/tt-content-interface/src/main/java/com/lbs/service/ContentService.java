package com.lbs.service;


import java.util.List;

import com.lbs.common.pojo.EasyUIDataGridResult;
import com.lbs.common.util.TaotaoResult;
import com.lbs.pojo.TbContent;

public interface ContentService {

	//查找
	EasyUIDataGridResult selectList(Integer page,Integer rows,Long categoryId);
	
	//新增
	TaotaoResult saveContent(TbContent content);
	
	//门户页大广告
	List<TbContent> selectTbcontent(long categoryId);
	
	//删除
	TaotaoResult deleteContent(long[] ids);
	
	//修改
	TaotaoResult editContent(TbContent content);
	
}
