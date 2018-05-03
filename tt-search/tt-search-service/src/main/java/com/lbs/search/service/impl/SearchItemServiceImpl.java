package com.lbs.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbs.common.pojo.SearchItem;
import com.lbs.common.util.TaotaoResult;
import com.lbs.search.mapper.SearchItemMapper;
import com.lbs.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService{

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrServer solr;
	
	@Override
	public TaotaoResult selectAll() {
		
		try {
			List<SearchItem> list = searchItemMapper.searchAll();
			for(SearchItem sea:list) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.setField("id",sea.getId() );
				doc.setField("item_title", sea.getTitle());
				doc.setField("item_sell_point",sea.getSell_point());
				doc.setField("item_price",sea.getPrice());
				doc.setField("item_image", sea.getImage());
				doc.setField("item_category_name", sea.getCategory_name());
				
				solr.add(doc);
			}
			solr.commit();
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "导入失败");
		} 

	}


	
	
}
