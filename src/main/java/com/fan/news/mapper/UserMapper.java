package com.fan.news.mapper;

import com.fan.news.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    int deleteByPrimaryKey(String userId);

    /**
     * 用户
     * @param record
     * @return
     */
    int insert(User record);
    int insertSelective(User record);


    User selectByPrimaryKey(String userId);

    /**
     * 用户登录
     * @param user
     * @return
     */
    User login(User user);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKeyWithBLOBs(User record);

    int updateByPrimaryKey(User record);
}