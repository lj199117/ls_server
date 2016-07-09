package com.nick.ls.util;

public class CommonUtils {
  /**
   * 
   * @param lat
   * @param lon  
   * @param raidus  半径
   * @return  最大、小的径维度
   */
	//SELECT p . * , s . * 
//	FROM prodouct p, shop s
//	WHERE p.shop_id = s.shop_id
//	AND s.shop_lon > 19.22931121943896ands.shop_lon < 119.23973478056104ands.shop_lat > 30.45220738013896ands.shop_lat < 120.46119261986104
//	LIMIT 0 , 30
	////http://localhost:8080/ls_server/servlet/NearbyServlet?lat=19.22931121943896&lon=119.234523&raidus=1000
	public static double[] getAroud(double lat,double lon,double raidus){
		//地球的周长是 24901公里
		//英里  和米的换算 单位： 1609 米
		//维度···································
		//1：计算地球一周每一度占多少米
		double degree=(24901*1609)/360.0;
		//计算维度上变化一米多少度
		double dpmLat=1/degree;
		// 计算搜索半径在维度的度数
		double raidusLat=dpmLat *raidus;
		double minLat=lat-raidusLat;
		double maxLat=lat+raidusLat;
		
		//经度···································
		//我们定位的地点的小圈上的距离变化
		double mpdLng=degree *Math.cos(lat*(Math.PI/180));
		double dpmLng=1/mpdLng;
		double raidusLng=dpmLng*raidus;
		double minLng=lon-raidusLng;
		double maxLng=lon+raidusLng;
		return new double []{minLat,minLng,maxLat,maxLng};
	}
	
}
