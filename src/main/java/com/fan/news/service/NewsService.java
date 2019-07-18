package com.fan.news.service;

import com.fan.news.pojo.News;
import com.fan.news.pojo.NewsFile;
import com.fan.news.pojo.NewsFileDto;
import com.fan.news.pojo.NewsImg;
import com.fan.news.utils.PageUtil;

public interface NewsService {
    int insert(News news) throws Exception;

    PageUtil getNewsById(Integer page, Integer pageSize, String userId);

    int insertNewsImg(NewsImg newsImg) throws Exception;

    PageUtil getHotNews(Integer page, Integer pageSize);

    NewsFileDto getNewsByNewsId(String newsId);

    int updateNews(News news) throws Exception;

    int insertNews(News news, NewsImg newsImg, NewsFile newsFile) throws Exception;

    int deleteNewsByNewsId(String newsId);
}
