package com.nick.ls.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.nick.ls.dao.impl.GoodsDao;
import com.nick.ls.dao.impl.GoodsDaoImpl;
import com.nick.ls.enity.Goods;
import com.nick.ls.enity.ResponseObject;

public class GoodsServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		GoodsDao dao=new GoodsDaoImpl();
		String cityId=request.getParameter("cityId");
		String catId=request.getParameter("catId");
		int page= Integer.parseInt(request.getParameter("page"));
		int size=Integer.parseInt(request.getParameter("size"));
		List<Goods> list= dao.getList(cityId, catId, page, size);
		//商品信息的总数
		double count=dao.getCount(cityId, catId);
		//将数据转为json字符串
		ResponseObject result=null;
		if (list!=null && list.size()>0) {
			result=new ResponseObject(1,list);
			result.setPage(page);
			result.setCount((int)Math.ceil(count/size));
			result.setSize(size);
		}else{
			result=new ResponseObject(0,"获取数据失败,请联系Nick大神");
		}
		out.println(new GsonBuilder().create().toJson(result));
		out.flush();
		out.close();


	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doGet(request, response);
	}

}
