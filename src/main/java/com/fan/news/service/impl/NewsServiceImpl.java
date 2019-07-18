package com.fan.news.service.impl;

import com.fan.news.mapper.NewsFileMapper;
import com.fan.news.mapper.NewsImgMapper;
import com.fan.news.mapper.NewsMapper;
import com.fan.news.pojo.*;
import com.fan.news.service.NewsService;
import com.fan.news.utils.PageUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    private NewsMapper newsMapper;
    @Autowired
    public void setNewsMapper(NewsMapper newsMapper) {
        this.newsMapper = newsMapper;
    }

    private NewsImgMapper newsImgMapper;
    @Autowired
    public void setNewsImgMapper(NewsImgMapper newsImgMapper) {
        this.newsImgMapper = newsImgMapper;
    }

    private NewsFileMapper newsFileMapper;
    @Autowired
    public void setNewsFileMapper(NewsFileMapper newsFileMapper) {
        this.newsFileMapper = newsFileMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(News news) throws Exception {
        int rows = 0;
        if (news != null) {
            rows = newsMapper.insert(news);
        }
        return rows;
    }

    @Override
    public PageUtil getNewsById(Integer page, Integer pageSize, String userId) {
        PageHelper.startPage(page, pageSize);
        List<News> newsList = newsMapper.selectByPrimaryKey(userId);
        PageUtil pageResult = new PageUtil();
        PageInfo<News> pageInfo = new PageInfo<>(newsList);

        pageResult.setPage(page);
        pageResult.setRecords(pageInfo.getTotal());
        pageResult.setRows(newsList);
        pageResult.setTotal(pageInfo.getPages());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertNewsImg(NewsImg newsImg) throws Exception {
        return newsImgMapper.insertImg(newsImg);
    }

    @Override
    public PageUtil getHotNews(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<NewsImgDto> newsList = newsImgMapper.getHotNews();
        PageInfo<NewsImgDto> newsImg = new PageInfo<>(newsList);

        PageUtil pageResult = new PageUtil();

        pageResult.setTotal(newsImg.getPages());
        pageResult.setRows(newsList);
        pageResult.setRecords(newsImg.getTotal());
        pageResult.setPage(page);
        return pageResult;
    }

    @Override
    public NewsFileDto getNewsByNewsId(String newsId) {
        return newsFileMapper.getNewsAndFile(newsId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateNews(News news) throws Exception {
        return newsMapper.updateByPrimaryKeySelective(news);
    }

    @Override
    public int insertNews(News news, NewsImg newsImg, NewsFile newsFile) throws Exception {
        int rows1 = 0, rows2 = 0, rows3 = 0;
        if (news != null) {
            rows1 = newsMapper.insert(news);
            if (newsImg != null) {
                rows2 = newsImgMapper.insertImg(newsImg);
            }
            if (newsFile != null) {
                rows3 = newsFileMapper.insert(newsFile);
            }
        }
        return (rows1 + rows2 + rows3);
    }

    @Override
    @Transactional
    public int deleteNewsByNewsId(String newsId) {
        return newsMapper.deleteByPrimaryKey(newsId);
    }
}
