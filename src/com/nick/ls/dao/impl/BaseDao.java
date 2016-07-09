package com.nick.ls.dao.impl;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import java.sql.Connection;
import java.sql.Statement;

public class BaseDao {
	private static String driver="com.mysql.jdbc.Driver";
	private static String url="jdbc:mysql://localhost:3306/ls?useUnicode=true&characterEncoding=utf8";
	private static String user="root";
	private static String password="root";
	static{
		try {
			//ResourceBundle bundle=ResourceBundle.getBundle("/ls_server/src/config.properties");
		//	String driver=bundle.getString("com.mysql.jdbc.Driver");
			
		//	password=bundle.getString("password");
		 	Class.forName(driver);
		} catch (Exception e) {
		}
	}

	protected Connection getConn() throws Exception{
		return new Inner().getConn();
	}
	
	protected  void close(ResultSet resultSet,Statement statement,Connection connection) throws Exception{
		new Inner().close(resultSet, statement, connection);
	}
	
	private class Inner implements Dao{
		public void close(ResultSet resultSet, Statement statement,
				Connection connection) throws Exception {
			  if (resultSet!=null) {
				resultSet.close();
				resultSet=null;
			}
			  if (statement!=null) {
				statement.close();
				statement=null;
			}
			  if (connection!=null) {
				connection.close();
				connection=null;
			}
			
			
			
		}
		public Connection getConn() throws Exception {
			return  DriverManager.getConnection(url,user,password);
		}

	}




}
