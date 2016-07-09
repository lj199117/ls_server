package com.nick.ls.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.nick.ls.dao.impl.CityDao;
import com.nick.ls.dao.impl.CityDaoImpl;
import com.nick.ls.enity.City;
import com.nick.ls.enity.ResponseObject;

public class CityServlet extends HttpServlet {
	public CityServlet() {
	}
	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		CityDao dao=new CityDaoImpl();
		PrintWriter out=response.getWriter();
		List<City> list= dao.getCity();
		ResponseObject result=null;
		if (list!=null && list.size()>0) {
			 result=new ResponseObject(1,list);
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
