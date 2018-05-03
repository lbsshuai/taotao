package com.lbs.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lbs.common.pojo.SearchItem;
import com.lbs.common.pojo.SearchResult;

@Repository
public class SearchResultdao {

	@Autowired
	private SolrServer solrServer;
	
	public SearchResult findByindex(SolrQuery query) throws SolrServerException {
		//使用solr查询
		QueryResponse response = solrServer.query(query);
		//得到查询结果
		SolrDocumentList docs = response.getResults();
		
		//获得查询商品总个数
		long totalCount = docs.getNumFound();
		SearchResult result = new SearchResult();
		result.setTotalCount(totalCount);
		
		//得到高亮数据,标题被高亮
		Map<String, Map<String, List<String>>> map = response.getHighlighting();
		
		List<SearchItem> items = new ArrayList<>();
		for(SolrDocument doc :docs) {
			SearchItem item = new SearchItem();
			item.setId((String)doc.get("id"));
			item.setSell_point((String)doc.get("item_sell_point"));
			item.setPrice((long)doc.get("item_price"));
			item.setImage((String)doc.get("item_image"));
			item.setCategory_name((String)doc.get("item_category_name"));
			
			
			//去高亮的标题
			List<String> title = map.get(doc.get("id")).get("item_title");
			if(title!=null&&title.size()>0) {
				item.setTitle(title.get(0));
			}else {
				item.setTitle((String)doc.get("item_title"));
			}
			items.add(item);
			
		}
		result.setItems(items);
		return result;
	}
}






















