package com.lbs.search.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbs.common.pojo.SearchItem;
import com.lbs.search.mapper.SearchItemMapper;

public class ItemAddMessageListener implements MessageListener{

	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String ss = textMessage.getText();
			long itemId = Long.parseLong(ss);
			
			Thread.sleep(100);
			SearchItem item = searchItemMapper.selectByItemId(itemId);
			
			SolrInputDocument doc = new SolrInputDocument();
			doc.setField("id", item.getId());
			doc.setField("item_title", item.getTitle());
			doc.setField("item_sell_point", item.getSell_point());
			doc.setField("itemimage", item.getImage());
			doc.setField("item_price", item.getPrice());
			doc.setField("item_category_name",item.getCategory_name());
			
			solrServer.add(doc);
			solrServer.commit();
			System.out.println("添加到solr上了/////////");
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
}
