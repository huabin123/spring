package com.my;

import org.springframework.stereotype.Service;

/**
 * @Author huabin
 * @DateTime 2023-03-05 18:04
 * @Desc
 */
@Service
public class TestBean {

	public String name = "Hello World123";

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
}
