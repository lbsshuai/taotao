package com.lbs.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbs.common.pojo.SearchResult;
import com.lbs.search.dao.SearchResultdao;
import com.lbs.service.SearchResultService;

@Service
public class SearchResultServiceImpl implements SearchResultService{

	@Autowired
	private SearchResultdao searchResultdao;
	
	@Override
	public SearchResult findItems(String key, int page, int rows) {
		//把搜索条件封装成SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置搜索神马
		query.setQuery(key);
		
		if(page<0)
			page=1;
		query.setStart((page-1)*rows);
		query.setRows(rows);
		
		//设置从哪个索引上搜
		query.set("df", "item_keywords");
		
		//设置标题被高亮及怎么高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");//设置哪个索引对应高亮
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		
		SearchResult result = null;
	
		try {
			result = searchResultdao.findByindex(query);
			long totalCount = result.getTotalCount();
			int totalPage = (int)(totalCount/rows);
			if(totalCount%rows!=0)
				totalPage++;
			result.setTotalPage(totalPage);
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		return result;
	}

}


















