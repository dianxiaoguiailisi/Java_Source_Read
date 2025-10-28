package com.zx.spring.Bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Bean的功能增强全部都是BeanPostProcessor+InitializingBean共同实现
 */
@Component
public class Person implements ApplicationContextAware {
	private String name;
	private ApplicationContext applicationContext;
	public Person() {
		System.out.println("person创建");
	}
	public void setName(String name) {
		this.name = name;
	}


	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public String getName() {
		return name;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
