package com.my;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author huabin
 * @DateTime 2023-05-19 16:58
 * @Desc
 */
@Component
public class TestBeanOverrider implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		TestBean testBean = (TestBean)beanFactory.getBean("testBean");
		testBean.addList("custom modify bean properties!");
	}
}
