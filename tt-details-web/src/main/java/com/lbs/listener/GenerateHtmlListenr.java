package com.lbs.listener;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.lbs.pojo.Item;
import com.lbs.pojo.TbItem;
import com.lbs.pojo.TbItemDesc;
import com.lbs.service.ItemService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class GenerateHtmlListenr implements MessageListener {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private FreeMarkerConfigurer freemarkerConfigurer;
	
	@Value("${GEN_HTML_UTL}")
	private String GEN_HTML_UTL;
	
	@Override
	public void onMessage(Message message) {
		try {
			//收到消息时生成静态页面
			TextMessage msg = (TextMessage) message;
			Long lg = new Long(msg.getText());
			
			Thread.sleep(100);
			
			//根据商品id查询商品的基本信息
			TbItem itemInfo = itemService.findById(lg.longValue());
			Item item = new Item(itemInfo);
			//根据商品id查询商品的描述信息
			TbItemDesc itemDesc = itemService.findDescById(lg.longValue());
			
			//创建数据集
			Map map = new HashMap();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			
			//根据FreeMarkerConfigurer对象得到Configuration对象
			Configuration config = freemarkerConfigurer.getConfiguration();
			Template template = config.getTemplate("item.ftl");
			
			//静态页面的路径和名称
			Writer out = new FileWriter(GEN_HTML_UTL+lg.longValue()+".html");
			template.process(map, out);
			
			out.close();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
