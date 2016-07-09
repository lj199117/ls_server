package com.nick.ls.dao.impl;

import java.sql.ResultSet;

import java.sql.Connection;
import java.sql.Statement;

public interface Dao {
   Connection getConn()throws Exception;
   void close(ResultSet resultSet,Statement statement,Connection connection)throws Exception;
   
	 
	
	
}
