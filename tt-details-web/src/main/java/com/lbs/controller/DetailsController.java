package com.lbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lbs.pojo.Item;
import com.lbs.pojo.TbItem;
import com.lbs.pojo.TbItemDesc;
import com.lbs.service.ItemService;

@Controller
public class DetailsController {

	@Autowired
	public ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String item(@PathVariable long itemId,Model model) {
		TbItem tbItem = itemService.findById(itemId);
		//把TbItem类型转为Item类型
		Item item = new Item(tbItem);
		
		TbItemDesc itemDesc = itemService.findDescById(itemId);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}
	
}
