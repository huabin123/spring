package com.my.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * @Author huabin
 * @DateTime 2023-05-19 13:53
 * @Desc
 */

public class MyEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private String msg;

	public MyEvent(Object source) {
		super(source);
	}

	public MyEvent(Object source, String msg) {
		super(source);
		this.msg = msg;
	}

	public void print(){
		System.out.println("============");
		System.out.println(msg);
		System.out.println("============");
	}
}
