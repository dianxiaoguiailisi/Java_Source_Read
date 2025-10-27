package com.zx.spring.Bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

//@Component
public class Person {

	private String name;

//	@Autowired 依赖的组件是多实例不能使用@A....
	private Chinese chinese;
	// 无参构造函数（必须添加）
	public Person() {
	}
	public void setChinese(Chinese chinese) {
		this.chinese = chinese;
	}


	@Lookup//去容器中找,@Bean这种方式注册时Person @Loolup不生效
	public Chinese getChinese() {
		return chinese;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				'}';
	}
}