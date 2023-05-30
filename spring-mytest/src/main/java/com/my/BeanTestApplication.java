package com.my;

import com.my.config.AppConfig;
import com.my.event.MyEvent;
import com.my.lifecycle.MyLifeCycle;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.ArrayList;

/**
 * @Author huabin
 * @DateTime 2023-03-05 18:06
 * @Desc
 */
public class BeanTestApplication {

	public static void main(String[] args) {
		// xml形式
//		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
//		System.out.println(classPathXmlApplicationContext.getBean(TestBean.class).getName());

		// 注解配置类形式
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
//		TestBean testBean = (TestBean)applicationContext.getBean("testBean");
		TestBean testBean = applicationContext.getBean("testBean", TestBean.class);
		System.out.println(testBean.getName());
		System.out.println(testBean.getList());

		applicationContext.publishEvent(new MyEvent("", "my events!"));

		/*
		conversionService是一个spring的类型转换器的总容器，比如说我们现在要将List类型转换为TestBean类型
		 */
		DefaultConversionService conversionService = (DefaultConversionService)DefaultConversionService.getSharedInstance();
		conversionService.addConverter(new MyConvert());
		TestBean convert = conversionService.convert(new ArrayList<String>(), TestBean.class);
		System.out.println("=================");
		System.out.println(convert.getName());
		System.out.println("=================");

		/*
		lifecycle专门处理我们的lifecycle子类。包含start方法和stop方法，可以由我们自由控制lifecycle子类的生命周期
		如果是TestBean这种没有实现lifecycle我们无法实时进行控制
		 */
		MyLifeCycle myLifeCycle = (MyLifeCycle)applicationContext.getBean("myLifeCycle");
		System.out.println(myLifeCycle.isRunning());  // false
		applicationContext.start();
		System.out.println(myLifeCycle.isRunning());  // true
		applicationContext.stop();
		System.out.println(myLifeCycle.isRunning());  // false
	}

}

