package com.hm.his.module.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.his.module.user.dao.UserMapper;
import com.hm.his.module.user.model.User;
import com.hm.his.module.user.service.UserService;

/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-3 15:19:47
 * @description 用户服务实现类
 * @version 3.0
 */
@Service("UserService")
public class UserServiceImpl implements UserService{

	@Autowired(required = false)
	UserMapper userMapper;
	
	@Override
	public Integer insertUser(User user) {
		Integer result = 0;
		try {
			result = userMapper.insertUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Integer deleteByUserId(Integer userId) {
		Integer result = 0;
		try {
			result = userMapper.deleteByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Integer updateUser(User user) {
		Integer result = 0;
		try {
			result = userMapper.updateUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public User getUserByOpenId(String openId) {
		User user = null;
		try {
			user = userMapper.getUserByOpenId(openId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
