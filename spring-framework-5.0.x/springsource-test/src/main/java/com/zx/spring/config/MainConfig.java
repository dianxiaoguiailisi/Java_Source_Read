package com.zx.spring.config;

import com.zx.spring.Bean.Cat;
import com.zx.spring.Bean.Person;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 这是一个配置类
 */
//@Configuration
////@Import({Person.class,MainConfig.MyImportRegistrar.class} )
//@ComponentScan("com.zx.spring")
public class MainConfig {
	/**
	 * 第一个注解导入方式
	 * @return
	 */
//	@Bean("person1")
//	public Person person() {
//		Person person = new Person();
//		person.setName("里斯");
//		return person;
//	}

	/**
	 *
	 *
	 * 1.直接写class:Person{name='null'}，直接利用无参构造器创建出对象放在容器中
	 * 2.ImportSelector
	 * 3.实现ImportBeanDefinitionRegistrar
	 * 		BeanDefinitionRegistry:bean定义信息的注册中心,里面都是bean的定义信息(BeanDefinition)
	 *
	 *
	 * 注意：这里设置的都是bean的定义信息（包括自动装配什么），不能赋值，
	 */
	 static class  MyImportRegistrar implements ImportBeanDefinitionRegistrar {

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
			//BeanDefindtion
			RootBeanDefinition catDefinition = new RootBeanDefinition();
			catDefinition.setBeanClass(Cat.class);
			registry.registerBeanDefinition("tomcat",catDefinition);
		}
	}
}

