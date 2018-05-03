package com.lbs.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.lbs.common.pojo.SearchResult;
import com.lbs.service.SearchResultService;

@Controller
public class SearchResultController {

	@Autowired 
	private SearchResultService searchResultService;
	/*
	 private static Logger logger = Logger.getLogger(BuyController.class);*/
	
	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;
	
	@RequestMapping("/search")
	public String find(String keyword,@RequestParam(defaultValue="1")Integer page,Model model) {
		
		try {
			if(StringUtils.isNotEmpty(keyword)) {
				keyword = new String(keyword.getBytes("ISO8859-1"),"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		SearchResult result = searchResultService.findItems(keyword, page, PAGE_ROWS);
		System.out.println(result+"-------------------------");
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages",result.getTotalPage());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount",result.getTotalCount());
		model.addAttribute("itemList", result.getItems());
		
		return  "search";
		
	}
}
























