package com.nick.ls.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nick.ls.enity.Goods;
import com.nick.ls.enity.Shop;

public class GoodsDaoImpl extends BaseDao implements GoodsDao {

	private String str="prodouct.prodouct_id, prodouct.category_id, prodouct.shop_id, prodouct.sub_category_id, prodouct.city_id, prodouct.prodouct_title, " +
	"prodouct.prodouct_image, prodouct.prodouct_start_time,prodouct.prodouct_sort_title,prodouct.prodouct_is_over_time,prodouct.prodouct_value, prodouct.prodouct_price," +
	"prodouct.prodouct_ribat, prodouct.prodouct_bought,prodouct.prodouct_minquota, prodouct.prodouct_maxquota," +
	" prodouct.prodouct_post, prodouct.prodouct_soldout, prodouct.prodouct_tip, prodouct.prodouct_end_time, " +
	"prodouct.prodouct_detail, prodouct.prodouct_is_refund," +
	" prodouct.prodouct_is_over_time AND shop.shop_id, " +
	"shop.shop_name, shop.shop_tel, shop.shop_address," +
	" shop.shop_area, shop.shop_open_time, shop.shop_lon," +
	" shop.shop_lat, shop.shop_traffic_info";

	public double getCount(String cityId, String catId) {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;

		try {
			connection=getConn();
			statement=connection.createStatement();
			String sql="select count(*) from prodouct where 1=1";
			if (org.apache.commons.lang3.StringUtils.isNotBlank(cityId)) {
				sql+="and city_id="+cityId;
			}
			if (org.apache.commons.lang3.StringUtils.isNotBlank(catId)) {
				sql+="and category_id="+catId;
			}
			resultSet=statement.executeQuery(sql);
			if (resultSet.next()) {
				int count=Integer.parseInt(resultSet.getString("count(*)"));
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
		return 0;
	}

	public List<Goods> getList(String cityId, String catId, int page, int size) {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		String sql="select "+str+" from prodouct,shop where prodouct.shop_id=shop.shop_id"+(org.apache.commons.lang3.StringUtils.isNotBlank(cityId)?(" and city_id= "+cityId):"")+(org.apache.commons.lang3.StringUtils.isNotBlank(catId)?(" and category_id="+catId):"")+" limit "+(page*size)+","+size;
		//		String sql="select "+str+" from prodouct,shop where prodouct.shop_id=shop.shop_id"+(org.apache.commons.lang3.StringUtils.isNotBlank(cityId)?("and city_id="+cityId):"")+(org.apache.commons.lang3.StringUtils.isNotBlank(catId)?("and category_id="+catId):("")+"limit"+(page*size)+","+size);

		List<Goods> goods=null;
		try {
			connection=getConn();
			statement=connection.createStatement();
			System.out.println("产品查询的sql语句:  "+sql);
			resultSet=statement.executeQuery(sql);
			goods=new ArrayList<Goods>();
			while (resultSet.next()) {
				Goods product=new Goods();
				product.setId(resultSet.getString("prodouct_id"));
				product.setCategoryId(resultSet.getString("category_id"));
				product.setShopId(resultSet.getString("shop_id"));
				product.setCityId(resultSet.getString("city_id"));
				product.setSortTitle(resultSet.getString("prodouct_sort_title"));
				product.setTitle(resultSet.getString("prodouct_title"));  
				product.setValue(resultSet.getString("prodouct_value"));
				product.setPrice(resultSet.getString("prodouct_price"));
				product.setRibat(resultSet.getString("prodouct_ribat"));
				product.setBought(resultSet.getString("prodouct_bought"));
				product.setMaxQuota(resultSet.getString("prodouct_maxquota"));
				product.setPost(resultSet.getString("prodouct_post"));
				product.setSoldOut(resultSet.getString("prodouct_soldout"));
				product.setTip(resultSet.getString("prodouct_tip"));
				product.setEndTime(resultSet.getString("prodouct_end_time"));
				product.setDetail(resultSet.getString("prodouct_detail"));
				product.setMinquota(resultSet.getString("prodouct_minquota"));
				product.setImgUrl(resultSet.getString("prodouct_image"));

				Shop shop=new Shop();
				shop.setId(resultSet.getString("shop_id"));
				shop.setName(resultSet.getString("shop_name"));
				shop.setAddress(resultSet.getString("shop_address"));
				shop.setArea(resultSet.getString("shop_area"));
				shop.setOpentime(resultSet.getString("shop_open_time"));
				shop.setTel(resultSet.getString("shop_tel"));
				shop.setLat(resultSet.getString("shop_lat"));
				shop.setLon(resultSet.getString("shop_lon"));
				product.setShop(shop);

				int overTime=resultSet.getInt("prodouct_is_over_time");
				int refund=resultSet.getInt("prodouct_is_refund");
				if (refund==1) {
					product.setRefund(true);
				}else if(refund==0){
					product.setRefund(false);
				}

				if (overTime==1) {
					product.setOverTime(true);
				}else if(overTime==0){
					product.setOverTime(false);
				}
				goods.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return goods;
	}
	//附近的商品
	public List<Goods> getCoodsByLBS(double lat ,double lon ,double d, double e, double f, double g) {
		String desc="GLength(LineString(PointFromWKB(POINT("+lat+","+lon+")),PonitFromWKB(POINT(shop.shop_lat,shop.shop_lon))))*69*1609.344 AS distance,";
		//		String sql="select "+ desc+ " p.*,s.* from prodouct p ,shop s where p.shop_id=s.shop_id and " +
		//		"s.shop_lon>"+e+
		//		"and s.shop_lon<"+g+
		//		"and s.shop_lat>"+d+
		//		"and s.shop_lat<"+f +" order by distance";
		String sql="select "+ desc+ " prodouct.*,shop.* from prodouct  ,shop  where prodouct.shop_id=shop.shop_id and " +
		"shop.shop_lon>"+e+
		"and shop.shop_lon<"+g+
		"and shop.shop_lat>"+d+
		"and shop.shop_lat<"+f +" order by distance";
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		System.out.println("sql:"+sql);
		List<Goods> resultList=new ArrayList<Goods>();
		try {
			connection=getConn();
			statement=connection.createStatement();
			resultSet=statement.executeQuery(sql);
			System.out.println("sql:"+sql);
			while(resultSet.next()) {
				Goods  product=new Goods();
				product.setId(resultSet.getString("prodouct_id"));
				product.setCategoryId(resultSet.getString("category_id"));
				product.setShopId(resultSet.getString("shop_id"));
				product.setCityId(resultSet.getString("city_id"));
				product.setSortTitle(resultSet.getString("prodouct_sort_title"));
				product.setTitle(resultSet.getString("prodouct_title"));  
				product.setValue(resultSet.getString("prodouct_value"));
				product.setPrice(resultSet.getString("prodouct_price"));
				product.setRibat(resultSet.getString("prodouct_ribat"));
				product.setBought(resultSet.getString("prodouct_bought"));
				product.setMaxQuota(resultSet.getString("prodouct_maxquota"));
				product.setPost(resultSet.getString("prodouct_post"));
				product.setSoldOut(resultSet.getString("prodouct_soldout"));
				product.setTip(resultSet.getString("prodouct_tip"));
				product.setEndTime(resultSet.getString("prodouct_end_time"));
				product.setDetail(resultSet.getString("prodouct_detail"));
				product.setMinquota(resultSet.getString("prodouct_minquota"));
				product.setImgUrl(resultSet.getString("prodouct_image"));

				Shop shop=new Shop();
				shop.setId(resultSet.getString("shop_id"));
				shop.setName(resultSet.getString("shop_name"));
				shop.setAddress(resultSet.getString("shop_address"));
				shop.setArea(resultSet.getString("shop_area"));
				shop.setOpentime(resultSet.getString("shop_open_time"));
				shop.setTel(resultSet.getString("shop_tel"));
				shop.setLat(resultSet.getString("shop_lat"));
				shop.setLon(resultSet.getString("shop_lon"));
				product.setShop(shop);
				resultList.add(product);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return resultList;
	}

	public List<Goods> getCoodsByLBS(double d, double e, double f, double g) {

		//String desc="GLength(LineString(PointFromWKB(POINT("+lat+","+lon+")),PonitFromWKB(POINT(shop.shop_lat,shop.shop_lon))))*69*1609.344 AS distance,";
		String sql="select p.*,s.* from prodouct p ,shop s where p.shop_id=s.shop_id and " +
		"s.shop_lon>"+e+
		"and s.shop_lon<"+g+
		"and s.shop_lat>"+d+
		"and s.shop_lat<"+f;
		//		String sql="select "+ desc+ " prodouct.*,shop.* from prodouct  ,shop  where prodouct.shop_id=shop.shop_id and " +
		//		"shop.shop_lon>"+e+
		//		"and shop.shop_lon<"+g+
		//		"and shop.shop_lat>"+d+
		//		"and shop.shop_lat<"+f +" order by distance";
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		System.out.println("sql:"+sql);
		List<Goods> resultList=new ArrayList<Goods>();
		try {
			connection=getConn();
			statement=connection.createStatement();
			resultSet=statement.executeQuery(sql);
			System.out.println("sql:"+sql);
			while(resultSet.next()) {
				Goods  product=new Goods();
				product.setId(resultSet.getString("prodouct_id"));
				product.setCategoryId(resultSet.getString("category_id"));
				product.setShopId(resultSet.getString("shop_id"));
				product.setCityId(resultSet.getString("city_id"));
				product.setSortTitle(resultSet.getString("prodouct_sort_title"));
				product.setTitle(resultSet.getString("prodouct_title"));  
				product.setValue(resultSet.getString("prodouct_value"));
				product.setPrice(resultSet.getString("prodouct_price"));
				product.setRibat(resultSet.getString("prodouct_ribat"));
				product.setBought(resultSet.getString("prodouct_bought"));
				product.setMaxQuota(resultSet.getString("prodouct_maxquota"));
				product.setPost(resultSet.getString("prodouct_post"));
				product.setSoldOut(resultSet.getString("prodouct_soldout"));
				product.setTip(resultSet.getString("prodouct_tip"));
				product.setEndTime(resultSet.getString("prodouct_end_time"));
				product.setDetail(resultSet.getString("prodouct_detail"));
				product.setMinquota(resultSet.getString("prodouct_minquota"));
				product.setImgUrl(resultSet.getString("prodouct_image"));

				Shop shop=new Shop();
				shop.setId(resultSet.getString("shop_id"));
				shop.setName(resultSet.getString("shop_name"));
				shop.setAddress(resultSet.getString("shop_address"));
				shop.setArea(resultSet.getString("shop_area"));
				shop.setOpentime(resultSet.getString("shop_open_time"));
				shop.setTel(resultSet.getString("shop_tel"));
				shop.setLat(resultSet.getString("shop_lat"));
				shop.setLon(resultSet.getString("shop_lon"));
				product.setShop(shop);
				resultList.add(product);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return resultList;

	}
	//分类
	public List<Goods> getCoodsByLBS(String caregoryId, double d, double e,
			double f, double g) {
		//String desc="GLength(LineString(PointFromWKB(POINT("+lat+","+lon+")),PonitFromWKB(POINT(shop.shop_lat,shop.shop_lon))))*69*1609.344 AS distance,";
		String sql="select p.*,s.* from prodouct p ,shop s where p.shop_id=s.shop_id  ";
		if (caregoryId==null ||"".equals(caregoryId)) {

		}
		else if(caregoryId.length()<4){
			sql+=" and p.category_id="+caregoryId;
		}else{
			sql+=" and p.sub_category_id="+caregoryId;
		}
		sql+=" and  s.shop_lon>"+e+
		" and s.shop_lon<"+g+
		" and s.shop_lat>"+d+
		" and s.shop_lat<"+f;
		//		String sql="select "+ desc+ " prodouct.*,shop.* from prodouct  ,shop  where prodouct.shop_id=shop.shop_id and " +
		//		"shop.shop_lon>"+e+
		//		"and shop.shop_lon<"+g+
		//		"and shop.shop_lat>"+d+
		//		"and shop.shop_lat<"+f +" order by distance";
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		System.out.println("sql:"+sql);
		List<Goods> resultList=new ArrayList<Goods>();
		try {
			connection=getConn();
			statement=connection.createStatement();
			resultSet=statement.executeQuery(sql);
			System.out.println("sql:"+sql);
			while(resultSet.next()) {
				Goods  product=new Goods();
				product.setId(resultSet.getString("prodouct_id"));
				product.setCategoryId(resultSet.getString("category_id"));
				product.setShopId(resultSet.getString("shop_id"));
				product.setCityId(resultSet.getString("city_id"));
				product.setSortTitle(resultSet.getString("prodouct_sort_title"));
				product.setTitle(resultSet.getString("prodouct_title"));  
				product.setValue(resultSet.getString("prodouct_value"));
				product.setPrice(resultSet.getString("prodouct_price"));
				product.setRibat(resultSet.getString("prodouct_ribat"));
				product.setBought(resultSet.getString("prodouct_bought"));
				product.setMaxQuota(resultSet.getString("prodouct_maxquota"));
				product.setPost(resultSet.getString("prodouct_post"));
				product.setSoldOut(resultSet.getString("prodouct_soldout"));
				product.setTip(resultSet.getString("prodouct_tip"));
				product.setEndTime(resultSet.getString("prodouct_end_time"));
				product.setDetail(resultSet.getString("prodouct_detail"));
				product.setMinquota(resultSet.getString("prodouct_minquota"));
				product.setImgUrl(resultSet.getString("prodouct_image"));

				Shop shop=new Shop();
				shop.setId(resultSet.getString("shop_id"));
				shop.setName(resultSet.getString("shop_name"));
				shop.setAddress(resultSet.getString("shop_address"));
				shop.setArea(resultSet.getString("shop_area"));
				shop.setOpentime(resultSet.getString("shop_open_time"));
				shop.setTel(resultSet.getString("shop_tel"));
				shop.setLat(resultSet.getString("shop_lat"));
				shop.setLon(resultSet.getString("shop_lon"));
				product.setShop(shop);
				resultList.add(product);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return resultList;


	}

	public List<Goods> getCoodsByLBS(int page, int size, String caregoryId,
			double d, double e, double f, double g) {
		//String desc="GLength(LineString(PointFromWKB(POINT("+lat+","+lon+")),PonitFromWKB(POINT(shop.shop_lat,shop.shop_lon))))*69*1609.344 AS distance,";
		String sql="select p.*,s.* from prodouct p ,shop s where p.shop_id=s.shop_id  ";
		if (caregoryId==null ||"".equals(caregoryId)) {

		}
		else if(caregoryId.length()<4){
			sql+=" and p.category_id="+caregoryId;
		}else{
			sql+=" and p.sub_category_id="+caregoryId;
		}
		sql+=" and  s.shop_lon>"+e+
		" and s.shop_lon<"+g+
		" and s.shop_lat>"+d+
		" and s.shop_lat<"+f;
		if (page==0) {
			page=1;
		}
		sql+=" limit "+((page-1)*size)+","+size;

		//limit page,size
		//		String sql="select "+ desc+ " prodouct.*,shop.* from prodouct  ,shop  where prodouct.shop_id=shop.shop_id and " +
		//		"shop.shop_lon>"+e+
		//		"and shop.shop_lon<"+g+
		//		"and shop.shop_lat>"+d+
		//		"and shop.shop_lat<"+f +" order by distance";
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		System.out.println("sql:"+sql);
		List<Goods> resultList=new ArrayList<Goods>();
		try {
			connection=getConn();
			statement=connection.createStatement();
			resultSet=statement.executeQuery(sql);
			System.out.println("sql:"+sql);
			while(resultSet.next()) {
				Goods  product=new Goods();
				product.setId(resultSet.getString("prodouct_id"));
				product.setCategoryId(resultSet.getString("category_id"));
				product.setShopId(resultSet.getString("shop_id"));
				product.setCityId(resultSet.getString("city_id"));
				product.setSortTitle(resultSet.getString("prodouct_sort_title"));
				product.setTitle(resultSet.getString("prodouct_title"));  
				product.setValue(resultSet.getString("prodouct_value"));
				product.setPrice(resultSet.getString("prodouct_price"));
				product.setRibat(resultSet.getString("prodouct_ribat"));
				product.setBought(resultSet.getString("prodouct_bought"));
				product.setMaxQuota(resultSet.getString("prodouct_maxquota"));
				product.setPost(resultSet.getString("prodouct_post"));
				product.setSoldOut(resultSet.getString("prodouct_soldout"));
				product.setTip(resultSet.getString("prodouct_tip"));
				product.setEndTime(resultSet.getString("prodouct_end_time"));
				product.setDetail(resultSet.getString("prodouct_detail"));
				product.setMinquota(resultSet.getString("prodouct_minquota"));
				product.setImgUrl(resultSet.getString("prodouct_image"));

				Shop shop=new Shop();
				shop.setId(resultSet.getString("shop_id"));
				shop.setName(resultSet.getString("shop_name"));
				shop.setAddress(resultSet.getString("shop_address"));
				shop.setArea(resultSet.getString("shop_area"));
				shop.setOpentime(resultSet.getString("shop_open_time"));
				shop.setTel(resultSet.getString("shop_tel"));
				shop.setLat(resultSet.getString("shop_lat"));
				shop.setLon(resultSet.getString("shop_lon"));
				product.setShop(shop);
				resultList.add(product);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return resultList;


	}

	public int getCoodsByLBSCount(String caregoryId, double d, double e,
			double f, double g) {
		String sql="select count(*) as count from prodouct p ,shop s where p.shop_id=s.shop_id  ";
		if (caregoryId==null ||"".equals(caregoryId)) {

		}
		else if(caregoryId.length()<4){
			sql+=" and p.category_id="+caregoryId;
		}else{
			sql+=" and p.sub_category_id="+caregoryId;
		}
		sql+=" and  s.shop_lon>"+e+
		" and s.shop_lon<"+g+
		" and s.shop_lat>"+d+
		" and s.shop_lat<"+f;
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		int count=0;
		System.out.println("sql:"+sql);
		try {
			connection=getConn();
			statement=connection.createStatement();
			resultSet=statement.executeQuery(sql);
			System.out.println("sql:"+sql);
			if(resultSet.next()){
				count=resultSet.getInt("count");
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}finally{
			try {
				close(resultSet, statement, connection);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return count;
	}
}
