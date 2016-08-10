package com.hm.his.module.user.dao;

import com.hm.his.module.user.model.User;

public interface UserMapper {

	User getUserByOpenId(String openId);
	
	Integer insertUser(User user);
    
	Integer deleteByUserId(Integer userId);

	Integer updateUser(User user);
}