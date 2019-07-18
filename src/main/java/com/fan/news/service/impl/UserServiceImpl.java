package com.fan.news.service.impl;

import com.fan.news.mapper.UserMapper;
import com.fan.news.pojo.User;
import com.fan.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(User user) throws Exception {
        return userMapper.updateByPrimaryKeySelective(user);
    }
}
