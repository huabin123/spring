package com.my.cycleDepends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author huabin
 * @DateTime 2023-05-22 13:57
 * @Desc
 */
@Component
public class ClassA implements IClassA{

	@Autowired
	private ClassB classB;

	public ClassA() {
		System.out.println("ClassA init success!!!");
	}

	public ClassA(ClassB classB) {
		this.classB = classB;
	}

	public ClassB getClassB() {
		return classB;
	}

	public void setClassB(ClassB classB) {
		this.classB = classB;
	}

	@Override
	public void execute() {
		System.out.println("i am Class A!");
	}
}
