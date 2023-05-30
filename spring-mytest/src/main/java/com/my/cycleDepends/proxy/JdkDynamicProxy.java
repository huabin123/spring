package com.my.cycleDepends.proxy;

import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author huabin
 * @DateTime 2023-05-23 09:04
 * @Desc
 */
public class JdkDynamicProxy implements InvocationHandler {

	private Object target;

	public JdkDynamicProxy(Object target) {
		this.target = target;
	}

	public <T> T getProxy(){
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(),
				(java.lang.reflect.InvocationHandler) this);
	}

	@Override
	public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
		return method.invoke(target, objects);
	}
}
