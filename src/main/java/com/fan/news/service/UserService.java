package com.fan.news.service;

import com.fan.news.pojo.User;

public interface UserService {
    User login(User user);

    int update(User user) throws Exception;
}
