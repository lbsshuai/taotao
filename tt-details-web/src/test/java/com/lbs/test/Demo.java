package com.lbs.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.lbs.common.pojo.Student;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class Demo {

	@Test
	public void test1() throws IOException, TemplateException {
		Configuration config = new Configuration(Configuration.getVersion());
		config.setDirectoryForTemplateLoading(new File("F:\\sts\\新建文件夹\\tt-details-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		config.setDefaultEncoding("UTF-8");
		Template template = config.getTemplate("first.ftl");
		
		Map map = new HashMap();
		map.put("hello", "word");
		
		FileWriter out = new FileWriter("F:\\新建文件夹 (2)\\first.html");
		template.process(map, out);
		out.close();
	}
	@Test
	public void test2() throws Exception, MalformedTemplateNameException, ParseException, IOException {
		Configuration config = new Configuration(Configuration.getVersion());
		config.setDirectoryForTemplateLoading(new File("F:\\sts\\新建文件夹\\tt-details-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		config.setDefaultEncoding("UTF-8");
		Template template = config.getTemplate("first.ftl");
		
		Map map = new HashMap();
		List<Student> list = new ArrayList<Student>();
		list.add(new Student(66, "赵四", 58, "铁岭"));
		list.add(new Student(66, "赵四", 58, "铁岭"));
		list.add(new Student(66, "赵四", 58, "铁岭"));
		list.add(new Student(66, "赵四", 58, "铁岭"));
		list.add(new Student(66, "赵四", 58, "铁岭"));
		map.put("stulist", list);
		map.put("date", new Date());
		map.put("val", null);
		FileWriter out = new FileWriter("F:\\新建文件夹 (2)\\student.html");
		template.process(map, out);
		out.close();
	}
}















