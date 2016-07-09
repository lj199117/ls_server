package com.nick.ls.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.GsonBuilder;
import com.nick.ls.dao.impl.UserDao;
import com.nick.ls.dao.impl.UserDaoimpl;
import com.nick.ls.enity.ResponseObject;
import com.nick.ls.enity.User;

public class UserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String name=request.getParameter("username");
System.out.println(name);
		String pwd=request.getParameter("password");
		String flag=request.getParameter("flag");
		UserDao uDao=new UserDaoimpl();
		ResponseObject result=null;
		if ("register".equals(flag)) {
			//注册
			if (!"".equals(name)&&!"".equals(pwd)) {
				User user=uDao.register(name, pwd);
				if (user!=null) {
					//注册成功
					result=new ResponseObject("注册成功",1,user);
				}else{
					result=new ResponseObject("注册失败,用户已经存在",0);
				}
			}else{
				result=new ResponseObject("注册失败",0);
			}
		}else if("login".equals(flag)){
			if (!"".equals(name)&&!"".equals(pwd)) {
				User user=uDao.login(name, pwd);
				if (user!=null) {
					//注册成功
					result=new ResponseObject("登录成功",1,user);
				}else{
					result=new ResponseObject("登录失败",0);
				}
			}else{
				result=new ResponseObject("登录失败",0);
			}
		}
		out.print(new GsonBuilder().create().toJson(result));
		out.flush();
		out.close();


	}

}
