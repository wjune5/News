package com.fan.news.mapper;

import com.fan.news.pojo.NewsFile;
import com.fan.news.pojo.NewsFileDto;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFileMapper {
    int insert(NewsFile newsFile);

    NewsFileDto getNewsAndFile(String newsId);
}
