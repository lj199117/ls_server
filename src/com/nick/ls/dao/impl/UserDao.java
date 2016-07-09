package com.nick.ls.dao.impl;

import com.nick.ls.enity.User;

public interface UserDao {
        
	User register(String name,String pwd);
	
	User login(String name,String pwd);
}
