package com.my.cycleDepends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author huabin
 * @DateTime 2023-05-22 13:57
 * @Desc
 */
@Component
public class ClassB {

	@Autowired
	private ClassA classA;

	public ClassB() {
		System.out.println("ClassB init success!!!");
	}

	public ClassB(ClassA classA) {
		this.classA = classA;
	}

	public ClassA getClassA() {
		return classA;
	}

	public void setClassA(ClassA classA) {
		this.classA = classA;
	}
}
