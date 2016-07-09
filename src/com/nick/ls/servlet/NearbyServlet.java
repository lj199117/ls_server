package com.nick.ls.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nick.ls.dao.impl.GoodsDao;
import com.nick.ls.dao.impl.GoodsDaoImpl;
import com.nick.ls.enity.Goods;
import com.nick.ls.enity.ResponseObject;
import com.nick.ls.util.CommonUtils;

public class NearbyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1980852883004745326L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		doPost(request, response);
	}
	//http://localhost:8080/ls_server/servlet/NearbyServlet?lat=39.983456&lon=116.3154950&raidus=1000&caregory=8

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		//输出附近商品的数据
		//维度
		String lat=request.getParameter("lat");
		//经度
		String lon=request.getParameter("lon");
		//搜索范围
		String radius=request.getParameter("radius");
		String caregory=request.getParameter("caregory");
		
		int page=0;
		int size=20;
		if (request.getParameter("page")!=null && !"".equals(request.getParameter("page"))) {
			page=Integer.parseInt(request.getParameter("page"));
		}
		if (request.getParameter("size")!=null && !"".equals(request.getParameter("size"))) {
			size=Integer.parseInt(request.getParameter("size"));
		}
		
		//按照指定的坐标和半径  计算最大、小纬度和经度(实际上是一个正方形)
		double [] around=CommonUtils.getAroud(Double.parseDouble(lat),Double.parseDouble(lon),Double.parseDouble(radius));
		// lon > mixLon && lon<maxLon && lat>minLat && lat<maxLat
		GoodsDao dao=new GoodsDaoImpl();
		///   List<Goods> list= dao.getCoodsByLBS(Double.parseDouble(lat) , Double.parseDouble(lon) ,around[0],around[1],around[2],around[3]);
		List<Goods> list= dao.getCoodsByLBS(page,size,caregory,around[0],around[1],around[2],around[3]);
		// List<Goods> list= dao.getCoodsByLBS(around[0],around[1],around[2],around[3]);
		System.out.println("------------------>"+caregory);
		ResponseObject result=null;
		if (list!=null && list.size()>0) {
			result=new ResponseObject(1,list);
		}else{
			result=new ResponseObject(0,"获取数据失败,请联系Nick大神····");
		}
		double count=dao.getCoodsByLBSCount(caregory,around[0],around[1],around[2],around[3]);
		result.setPage(page<1?1:page);
		result.setSize(size);
		result.setCount((int)Math.ceil(count/size));
		out.println(new GsonBuilder().create().toJson(result));
		out.flush();
		out.close();

	}

}
