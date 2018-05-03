package com.lbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbs.pojo.TbItem;
import com.lbs.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/item/{id}")
	@ResponseBody
	public TbItem select(@PathVariable Long id) {
		TbItem item = itemService.findById(id);
		return item;
	}
	
}












