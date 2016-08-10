package com.hm.his.module.user.service;

import com.hm.his.module.user.model.User;



/**
 * @author ZhouPengyu
 * @company H.M
 * @date 2016-5-3 15:20:31
 * @description user服务接口
 * @version 3.0
 */
public interface UserService {
	
	User getUserByOpenId(String openId);
	
	Integer insertUser(User user);
    
	Integer deleteByUserId(Integer userId);

	Integer updateUser(User user);
	
}
