package com.my.lifecycle;

import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Component;

/**
 * @Author huabin
 * @DateTime 2023-05-22 12:13
 * @Desc
 */
@Component
public class MyLifeCycle implements Lifecycle {

	private boolean running;

	@Override
	public void start() {
		System.out.println("=========");
		System.out.println("my life cycle start!");
		this.running = true;
	}

	@Override
	public void stop() {
		System.out.println("=========");
		System.out.println("my life cycle stop!");
		this.running = false;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}
}
