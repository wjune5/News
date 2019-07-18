package com.fan.news.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewsImgDto {
    private String newsId;

    private String content;

    private String title;

    private String userId;

    private Date lastEditTime;

    private Date createTime;

    private String nickname;

    private List<NewsImg> newsImgList = new ArrayList<>();

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<NewsImg> getNewsImgList() {
        return newsImgList;
    }

    public void setNewsImgList(List<NewsImg> newsImgList) {
        this.newsImgList = newsImgList;
    }
}
