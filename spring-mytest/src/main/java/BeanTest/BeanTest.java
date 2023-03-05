package BeanTest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author huabin
 * @DateTime 2023-03-05 18:06
 * @Desc
 */
public class BeanTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
		System.out.println(classPathXmlApplicationContext.getBean(TestBean.class).getName());
	}

}

