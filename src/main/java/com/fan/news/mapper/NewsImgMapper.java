package com.fan.news.mapper;

import com.fan.news.pojo.NewsImg;
import java.util.List;

import com.fan.news.pojo.NewsImgDto;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsImgMapper {

    int deleteByPrimaryKey(Integer newsImgId);
    
    int insertImg(NewsImg newsImg);

    int insertSelective(NewsImg record);

    NewsImg selectByPrimaryKey(Integer newsImgId);

    int updateByPrimaryKeySelective(NewsImg record);

    int updateByPrimaryKey(NewsImg record);

    List<NewsImgDto> getHotNews();
}