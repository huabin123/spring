package com.my.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author huabin
 * @DateTime 2023-05-19 14:07
 * @Desc
 */
@Component
public class MyListener implements ApplicationListener<ApplicationEvent> {
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof MyEvent) {
			((MyEvent) event).print();
		}
	}
}
