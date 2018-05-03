package com.lbs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lbs.pojo.TbContent;
import com.lbs.service.ContentService;

@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@Value("${CONTENT_AD}")
	private long CONTENT_AD ;
	
	@RequestMapping("/index")
	public String index(Model model) {
		Long categoryId = CONTENT_AD;
		System.out.println(categoryId);
		List<TbContent> list = contentService.selectTbcontent(categoryId);
		System.out.println(list);
		model.addAttribute("ad1List", list);
		return "index";
	}
}
