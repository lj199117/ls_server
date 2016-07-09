package com.nick.ls.enity;

public class ResponseObject {
   
	private String msg;//返回信息
	private int state=1;//状态   0：失败   1：成功
	private Object datas;//存放解析数据
	private int page;
	private int count;
	private int size;
	
	
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public ResponseObject(String msg, int state, Object datas) {
		this.msg = msg;
		this.state = state;
		this.datas = datas;
	}
	//失败
	public ResponseObject(int state, Object datas) {
		this.state = state;
		this.datas = datas;
	}
	//chengg
	public ResponseObject(String msg, int state) {
	 
		this.msg = msg;
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Object getDatas() {
		return datas;
	}
	public void setDatas(Object datas) {
		this.datas = datas;
	}
	
	
	 
	
	
	
}
