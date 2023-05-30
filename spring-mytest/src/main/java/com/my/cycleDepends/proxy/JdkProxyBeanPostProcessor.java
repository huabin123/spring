package com.my.cycleDepends.proxy;

import com.my.cycleDepends.ClassA;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Author huabin
 * @DateTime 2023-05-23 09:17
 * @Desc
 */
@Component
public class JdkProxyBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

	public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		if (bean instanceof ClassA) {
			JdkDynamicProxy jdkDynamicProxy = new JdkDynamicProxy(bean);
			return jdkDynamicProxy.getProxy();
		}
		return bean;
	}

}
