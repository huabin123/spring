package com.my.cycleDepends;

import com.my.cycleDepends.proxy.JdkProxyBeanPostProcessor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author huabin
 * @DateTime 2023-05-22 14:14
 * @Desc 三级缓存
 */
public class MainTest3 {

	private static Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

	// 定义一个（一级）缓存，这个缓存中需要保存我们的bean对象，避免多次创建
	private static Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

	private static Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(256);

	/*
	现在，我们发现存在循环依赖的情况下如果还存在方法增强（AOP）会存在一些问题，如果一个类有AOP，如果直接使用二级缓存
	会导致每次拿到的都是最初的那个Instance，我们应该要拿到每次动态代理生成的类。然而spring的生命周期的顺序是：
	实例化 -- 属性填充（进行循环依赖的属性填充） -- 初始化 -- aop（aop每次可能生成不同的代理对象，可是由于aop的位置
	太靠后了，没办法干预到属性填充，就导致属性填充过程总没办法用到aop生成的新的代理类，导致依赖注入的错误）
	所以，我们需要引入三级缓存，这个三级缓存，需要"有实力"将我们的AOP操作，移动到实例化之后，属性填充之前。
	所以，我们的三级缓存，需要有延迟加载的功能（钩子，函数式编程，回调方法）
	 */
	private static Map<String, ObjectFactory> singletonFactories = new ConcurrentHashMap<>(256);

	// 当前循环依赖的bean是否在创建中
	private static Set<String> singletonCurrentlyInCreation = new HashSet<>();

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
		ClassA classA = (ClassA)getBean("classA");
		classA.execute();
	}

	private static Object getBean(String beanName) throws Exception {

		Object bean = getSingleton(beanName);
		if (bean != null) {
			return bean;
		}

		if (!singletonCurrentlyInCreation.contains(beanName)) {
			singletonCurrentlyInCreation.add(beanName);
		}

		// 第一步：实例化 进行Bean的实例化,这里是RootBeanDefinition，不是BeanDefinition
		RootBeanDefinition bd = (RootBeanDefinition)beanDefinitionMap.get(beanName);
		Class<?> clazz = bd.getBeanClass();  // 这里是getBeanClass不是getClass
		// 调用了无参构造
		Object instance = clazz.newInstance();

		/*
		这里如果只用singletonObjects一个缓存将至少导致两个问题
		1、singletonObjects存放的是不完整对象，这时候如果有别的线程调用getBean()会得到不完整的对象，而且singletonObjects将会
		存在两种对象，存在循环依赖的不完整对象和不存在循环依赖的完整对象
		2、singletonObjects如果只存完整对象，将无法从根源上解决循环依赖问题

		解决方案：引入二级缓存
		 */
//		singletonObjects.put(beanName, instance);
		earlySingletonObjects.put(beanName, instance);

		/*
		如果有循环依赖并且有aop代理，必须将Class A的方法增强挪到这里，并且延迟添加
		 */
		if (singletonCurrentlyInCreation.contains(beanName)) {
			// 这个put要在调用ObjectFactory.getObject时才会调用
			singletonFactories.put(beanName,
					() -> new JdkProxyBeanPostProcessor().getEarlyBeanReference(earlySingletonObjects.get(beanName), beanName));
		}

		// 第二步：属性填充
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

		// 第三步： 初始化（调用一些init-method）


		// 第四步：方法增强（AOP）
		/*
		此时存在的问题：如果既有循环依赖又有aop代理对象的创建，代理对象将没有地方能保存
		 */



		// 添加到缓存
		/*
		刚开始，spring认为，这个缓存已经足够，因为每个对象的创建完成之后，都会得到一个完整的对象，可以放到一个缓存中，
		但是他没有考虑到一些问题。在这里调用main函数将发生无限递归调用，栈将溢出。
		解决方案：将put操作提前到属性填充之前
		 */
		singletonObjects.put(beanName, instance);
		earlySingletonObjects.remove(beanName);
		singletonFactories.remove(beanName);
		return instance;
	}

	private static Object getSingleton(String beanName) {
		Object singleton = singletonObjects.get(beanName);
		// 一级缓存中没有且正在生成
		if (singleton == null && singletonCurrentlyInCreation.contains(beanName)) {
			singleton = earlySingletonObjects.get(beanName);
			if (singleton == null) {
				// 二级缓存没有，去三级缓存拿
				ObjectFactory factory = singletonFactories.get(beanName);
				if (factory != null) {
					singleton = factory.getObject();
					earlySingletonObjects.put(beanName, singleton);
					singletonFactories.remove(beanName);
				}
			}
		}
		return singleton;
	}

}
