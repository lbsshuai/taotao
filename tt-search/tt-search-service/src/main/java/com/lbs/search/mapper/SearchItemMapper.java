package com.lbs.search.mapper;

import java.util.List;

import com.lbs.common.pojo.SearchItem;

public interface SearchItemMapper {

	List<SearchItem> searchAll();
	
	SearchItem selectByItemId(long itemId);
}
