package com.lbs.pojo;

import org.apache.commons.lang3.StringUtils;

public class Item extends TbItem{

	public Item(TbItem tbItem) {
		this.setId(tbItem.getId());
		this.setTitle(tbItem.getTitle());
		this.setSellPoint(tbItem.getSellPoint());
		this.setPrice(tbItem.getPrice());
		this.setNum(tbItem.getNum());
		this.setBarcode(tbItem.getBarcode());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
		this.setStatus(tbItem.getStatus());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
		
	}
	
	public String[] getImages() {
		String img = this.getImage();
		if(StringUtils.isNotBlank(img)) {
			String[] images = getImage().split(",");
			return images;
		}
		return null;
		
	}
	
	
	
}
