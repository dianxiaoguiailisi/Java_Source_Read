package com.zx.spring.MainTest;

import com.zx.spring.Bean.Cat;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FactoryTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		Cat bean = context.getBean(Cat.class);
		System.out.println(bean);
	}
}
