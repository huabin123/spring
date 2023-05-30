package com.my;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author huabin
 * @DateTime 2023-03-05 18:04
 * @Desc
 */
@Service
public class TestBean {

	public String name = "Hello World123";

	private List<String> list = new ArrayList<>();

	public TestBean(String name) {
		this.name = name;
	}

	public TestBean() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getList(){
		return this.list;
	}

	public void addList(String s){
		this.list.add(s);
	}
}
