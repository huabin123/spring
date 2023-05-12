package com.my;

import com.my.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author huabin
 * @DateTime 2023-03-05 18:06
 * @Desc
 */
public class BeanTest {

	public static void main(String[] args) {
		// xml形式
//		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
//		System.out.println(classPathXmlApplicationContext.getBean(TestBean.class).getName());

		// 注解配置类形式
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		TestBean testBean = (TestBean)applicationContext.getBean("testBean");
		System.out.println(testBean.getName());
	}

}

