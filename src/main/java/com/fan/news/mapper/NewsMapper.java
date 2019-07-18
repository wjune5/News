package com.fan.news.mapper;

import com.fan.news.pojo.News;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsMapper {

    int deleteByPrimaryKey(String newsId);

    int insert(News record);

    int insertSelective(News record);

    List<News> selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(News record);

    int updateByPrimaryKey(News record);

    News getNewsByNewsId(String newsId);
}