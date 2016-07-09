package com.nick.ls.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.nick.ls.enity.User;

public class UserDaoimpl extends BaseDao implements UserDao {
	public User register(String name, String pwd) {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		try {
			connection=getConn();
			statement=connection.createStatement();
			String sqlForCheck="select * from user where user_name= "+"'"+name+"'";
			System.out.println(sqlForCheck);
			resultSet=statement.executeQuery(sqlForCheck);
			if (resultSet.next()) {
				return null;
			}else{
				//插入的sql
				String sqlForInsert="insert into user (user_name,user_login_pwd) values ('"+name+"','"+pwd+"')";
				System.out.println(sqlForInsert);
				statement.execute(sqlForInsert);
				//检测是否添加进了数据库
				resultSet=statement.executeQuery(sqlForCheck);
				if (resultSet.next()) {
					User user=new User();
					user.setId(resultSet.getString("user_id"));
					user.setName(resultSet.getString("user_name"));
					user.setLoginPwd(resultSet.getString("user_login_pwd"));
					user.setPayPwd(resultSet.getString("user_pay_pwd"));
					user.setTel(resultSet.getString("user_tel"));
					return user;
				}
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public User login(String name, String pwd) {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		try {
			connection=getConn();
			statement=connection.createStatement();
			String sqlForCheck="select * from user where user_name= "+"'"+name+"' and user_login_pwd='"+pwd+"'";
			System.out.println(sqlForCheck);
			resultSet=statement.executeQuery(sqlForCheck);
			if (resultSet.next()) {
				User user=new User();
				user.setId(resultSet.getString("user_id"));
				user.setName(resultSet.getString("user_name"));
				user.setLoginPwd(resultSet.getString("user_login_pwd"));
				user.setPayPwd(resultSet.getString("user_pay_pwd"));
				user.setTel(resultSet.getString("user_tel"));
				return user;
			}
		} catch (Exception e) {
		    e.printStackTrace();
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
