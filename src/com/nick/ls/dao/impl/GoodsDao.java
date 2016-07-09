package com.nick.ls.dao.impl;

import java.util.List;

import com.nick.ls.enity.Goods;

public interface GoodsDao {
	//catId 分类id
    public List<Goods> getList(String cityId,String catId,int page,int size);
    
    
    public double getCount(String cityId,String catId );


	public List<Goods> getCoodsByLBS(double lat ,double lon ,double d, double e, double f, double g);
	public List<Goods> getCoodsByLBS(double d, double e, double f, double g);
	public List<Goods> getCoodsByLBS(String caregory_id,double d, double e, double f, double g);


	public List<Goods> getCoodsByLBS(int page, int size, String caregory,
			double d, double e, double f, double g);
	
	public int getCoodsByLBSCount(String caregory,
			double d, double e, double f, double g);
}
