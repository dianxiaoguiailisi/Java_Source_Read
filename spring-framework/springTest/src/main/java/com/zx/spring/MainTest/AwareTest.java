package com.zx.spring.MainTest;

import com.zx.spring.Bean.Cat;
import com.zx.spring.Bean.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AwareTest {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		Person bean = context.getBean(Person.class);
		ApplicationContext applicationContext = bean.getApplicationContext();
		System.out.println(context == applicationContext);
	}
}
