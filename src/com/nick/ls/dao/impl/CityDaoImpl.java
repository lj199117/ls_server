package com.nick.ls.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import com.nick.ls.enity.City;
public class CityDaoImpl extends BaseDao implements CityDao {
	public List<City> getCity() {
		Connection connection=null;
		java.sql.Statement statement=null;
		ResultSet resultSet=null;
		List<City> cities=null;
		try {
			connection=getConn();
			statement=connection.createStatement();
			resultSet=statement.executeQuery("select * from  city order by city_sortkey");
			cities=new ArrayList<City>();
			while (resultSet.next()) {
				 City city=new City();
				 city.setId(resultSet.getString("city_id"));
				 city.setName(resultSet.getString("city_name"));
				 city.setSortKey(resultSet.getString("city_sortkey"));
				 cities.add(city);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cities;
	}
}
