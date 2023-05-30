package com.my.cycleDepends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author huabin
 * @DateTime 2023-05-22 14:14
 * @Desc 引入一级缓存，暴露循环依赖问题
 */
public class MainTest1 {

	private static Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

	// 定义一个（一级）缓存，这个缓存中需要保存我们的bean对象，避免多次创建
	private static Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

	public static void loadBeanDefinitions(){
		RootBeanDefinition abd = new RootBeanDefinition(ClassA.class);
		RootBeanDefinition bbd = new RootBeanDefinition(ClassB.class);
		beanDefinitionMap.put("classA", abd);
		beanDefinitionMap.put("classB", bbd);
	}

	public static void main(String[] args) throws Exception {
		// 先把BeanDefinitions都加载进来
		loadBeanDefinitions();
		for (String beanName : beanDefinitionMap.keySet()) {
			getBean(beanName);
		}
	}

	private static Object getBean(String beanName) throws Exception {
		// 进行Bean的实例化,这里是RootBeanDefinition，不是BeanDefinition
		RootBeanDefinition bd = (RootBeanDefinition)beanDefinitionMap.get(beanName);
		Class<?> clazz = bd.getBeanClass();  // 这里是getBeanClass不是getClass
		// 调用了无参构造
		Object instance = clazz.newInstance();

		// 属性填充
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			Autowired annotation = f.getAnnotation(Autowired.class);
			if (annotation != null) {
				f.setAccessible(true);
				String name = f.getName();
				Object dep = getBean(name);
				f.set(instance, dep);
			}
		}
		// 添加到缓存
		/*
		刚开始，spring认为，这个缓存已经足够，因为每个对象的创建完成之后，都会得到一个完整的对象，可以放到一个缓存中，
		但是他没有考虑到一些问题。在这里调用main函数将发生无限递归调用，栈将溢出
		 */
		singletonObjects.put(beanName, instance);
		return instance;
	}

}
