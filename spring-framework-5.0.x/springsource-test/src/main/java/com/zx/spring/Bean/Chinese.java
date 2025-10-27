package com.zx.spring.Bean;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
//@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
////容器应该给cat再创建一个实例对象
//@Component
public class Chinese {
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
