package com.lbs.service;


import com.lbs.common.pojo.SearchResult;

public interface SearchResultService {

	SearchResult  findItems(String key,int page,int rows);
}
