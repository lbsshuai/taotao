package com.lbs.solr.test;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrDemo {

	@Test
	public void test1() throws SolrServerException, IOException {
		SolrServer solr = new HttpSolrServer("http://123.206.95.34:8080/solr");
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", "3388");
		doc.setField("title", "王者荣sdfsdfsdf耀");
		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.setField("id", "338");
		doc1.setField("title", "王者f耀");
		solr.add(doc);
		solr.add(doc1);
		solr.commit();
	}
	
	@Test
	public void test2() throws SolrServerException, IOException {
		SolrServer solr = new HttpSolrServer("http://123.206.95.34:8080/solr");
		solr.deleteByQuery("title:王者荣sdfsdfsdf耀");
		solr.commit();
	}
}






















