/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * 扩展 {@link BeanFactory} 接口，由 bean 工厂实现
 * 可以枚举其所有 bean 实例，而不是尝试查找 bean
 * 根据客户要求逐一按名称。BeanFactory 实现
 * 预加载所有 bean 定义（例如基于 XML 的工厂）可以实现
 * 此接口。
 *
 * <p>如果这是一个 {@link HierarchicalBeanFactory}，则返回值<i>不会</i>
 * 考虑任何 BeanFactory 层次结构，但仅与 Bean 相关
 * 在当前工厂中定义。使用 {@link BeanFactoryUtils} 帮助程序类
 * 也要考虑祖先工厂中的豆子。
 *
 * <p>此接口中的方法将仅遵循该工厂的 bean 定义。
 * 他们将忽略任何已通过其他方式注册的单例 Bean，例如
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory} 的
 * {@code registerSingleton} 方法，但
 * {@code getBeanNamesOfType} 和 {@code getBeansOfType} 将检查
 * 此类手动注册的单例也是如此。当然，BeanFactory 的 {@code getBean}
 * 也允许透明地访问此类特殊豆子。然而，在典型的
 * 场景，无论如何，所有 bean 都将由外部 bean 定义定义，因此大多数
 * 应用程序无需担心这种差异化。
 */
public interface ListableBeanFactory extends BeanFactory {

/**
	 * 检查此 Bean 工厂是否包含具有给定名称的 Bean 定义。
	 * <p>不考虑本厂可能参与的任何等级制度，
	 * 并忽略已由
	 * bean 定义以外的其他方法。
	 * @param  bean命名要查找的 bean 的名称
	 * @return此 Bean 工厂是否包含具有给定名称的 Bean 定义
	 * @see #containsBean
	 */
	boolean containsBeanDefinition(String beanName);

	/**
	 * 返回工厂中定义的 bean 数量。
	 * <p>不考虑本厂可能参与的任何等级制度，
	 * 并忽略已由
	 * bean 定义以外的其他方法。
	 * @return工厂中定义的豆数
	 */
	int getBeanDefinitionCount();

	/**
	 * 返回此工厂中定义的所有 bean 的名称。
	 * <p>不考虑本厂可能参与的任何等级制度，
	 * 并忽略已由
	 * bean 定义以外的其他方法。
	 * @return此工厂中定义的所有 bean 的名称，
	 * 或空数组（如果未定义）
	 */
	String[] getBeanDefinitionNames();

/**
	 * 返回与给定类型匹配的 bean（包括子类）的名称，
	 * 从 bean 定义或 {@code getObjectType} 的值来看
	 * 对于 FactoryBeans。
	 * <p><b>注意：此方法仅内省顶级 bean。</b>它<i>不会</i>
	 * 检查可能也与指定类型匹配的嵌套 bean。
	 * <p>确实考虑了 FactoryBeans 创建的对象，这意味着 FactoryBeans
	 * 将被初始化。如果 FactoryBean 创建的对象不匹配，
	 * 原始 FactoryBean 本身将与类型匹配。
	 * <p>不考虑本工厂可能参与的任何等级制度。
	 * 使用 BeanFactoryUtils 的 {@code beanNamesForTypeIncludingAncestors}
	 * 在祖先工厂中也包括豆类。
	 * <p>注意：<i>不</i>忽略已注册的单例 Bean
	 * 通过 bean 定义以外的其他方式。
	 * <p>此版本的 {@code getBeanNamesForType} 匹配所有类型的 bean，
	 * 无论是单例、原型还是 FactoryBeans。在大多数实现中，
	 * 结果将与 {@code getBeanNamesForType（type， true， true）} 相同。
	 * <p>此方法返回的 Bean 名称应始终返回<i>
	 *</i> 后端配置中的定义顺序，尽可能。
	 * @param键入要匹配的通用类型类或接口
	 * @return 匹配的 bean（或 FactoryBeans 创建的对象）的名称
	 * 给定的对象类型（包括子类），如果没有，则为空数组
	 * @since 4.2
	 * @see #isTypeMatch（字符串，可解析类型）
	 * @see FactoryBean#getObjectType
	 * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors（ListableBeanFactory， ResolvableType）
	 */
	String[] getBeanNamesForType(ResolvableType type);

	/**
	 * Return the names of beans matching the given type (including subclasses),
	 * judging from either bean definitions or the value of {@code getObjectType}
	 * in the case of FactoryBeans.
	 * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
	 * check nested beans which might match the specified type as well.
	 * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
	 * will get initialized. If the object created by the FactoryBean doesn't match,
	 * the raw FactoryBean itself will be matched against the type.
	 * <p>Does not consider any hierarchy this factory may participate in.
	 * Use BeanFactoryUtils' {@code beanNamesForTypeIncludingAncestors}
	 * to include beans in ancestor factories too.
	 * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
	 * by other means than bean definitions.
	 * <p>This version of {@code getBeanNamesForType} matches all kinds of beans,
	 * be it singletons, prototypes, or FactoryBeans. In most implementations, the
	 * result will be the same as for {@code getBeanNamesForType(type, true, true)}.
	 * <p>Bean names returned by this method should always return bean names <i>in the
	 * order of definition</i> in the backend configuration, as far as possible.
	 * @param type the class or interface to match, or {@code null} for all bean names
	 * @return the names of beans (or objects created by FactoryBeans) matching
	 * the given object type (including subclasses), or an empty array if none
	 * @see FactoryBean#getObjectType
	 * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors(ListableBeanFactory, Class)
	 */
	String[] getBeanNamesForType(@Nullable Class<?> type);

	/**
	 * Return the names of beans matching the given type (including subclasses),
	 * judging from either bean definitions or the value of {@code getObjectType}
	 * in the case of FactoryBeans.
	 * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
	 * check nested beans which might match the specified type as well.
	 * <p>Does consider objects created by FactoryBeans if the "allowEagerInit" flag is set,
	 * which means that FactoryBeans will get initialized. If the object created by the
	 * FactoryBean doesn't match, the raw FactoryBean itself will be matched against the
	 * type. If "allowEagerInit" is not set, only raw FactoryBeans will be checked
	 * (which doesn't require initialization of each FactoryBean).
	 * <p>Does not consider any hierarchy this factory may participate in.
	 * Use BeanFactoryUtils' {@code beanNamesForTypeIncludingAncestors}
	 * to include beans in ancestor factories too.
	 * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
	 * by other means than bean definitions.
	 * <p>Bean names returned by this method should always return bean names <i>in the
	 * order of definition</i> in the backend configuration, as far as possible.
	 * @param type the class or interface to match, or {@code null} for all bean names
	 * @param includeNonSingletons whether to include prototype or scoped beans too
	 * or just singletons (also applies to FactoryBeans)
	 * @param allowEagerInit whether to initialize <i>lazy-init singletons</i> and
	 * <i>objects created by FactoryBeans</i> (or by factory methods with a
	 * "factory-bean" reference) for the type check. Note that FactoryBeans need to be
	 * eagerly initialized to determine their type: So be aware that passing in "true"
	 * for this flag will initialize FactoryBeans and "factory-bean" references.
	 * @return the names of beans (or objects created by FactoryBeans) matching
	 * the given object type (including subclasses), or an empty array if none
	 * @see FactoryBean#getObjectType
	 * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors(ListableBeanFactory, Class, boolean, boolean)
	 */
	String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);

	/**
	 * Return the bean instances that match the given object type (including
	 * subclasses), judging from either bean definitions or the value of
	 * {@code getObjectType} in the case of FactoryBeans.
	 * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
	 * check nested beans which might match the specified type as well.
	 * <p>Does consider objects created by FactoryBeans, which means that FactoryBeans
	 * will get initialized. If the object created by the FactoryBean doesn't match,
	 * the raw FactoryBean itself will be matched against the type.
	 * <p>Does not consider any hierarchy this factory may participate in.
	 * Use BeanFactoryUtils' {@code beansOfTypeIncludingAncestors}
	 * to include beans in ancestor factories too.
	 * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
	 * by other means than bean definitions.
	 * <p>This version of getBeansOfType matches all kinds of beans, be it
	 * singletons, prototypes, or FactoryBeans. In most implementations, the
	 * result will be the same as for {@code getBeansOfType(type, true, true)}.
	 * <p>The Map returned by this method should always return bean names and
	 * corresponding bean instances <i>in the order of definition</i> in the
	 * backend configuration, as far as possible.
	 * @param type the class or interface to match, or {@code null} for all concrete beans
	 * @return a Map with the matching beans, containing the bean names as
	 * keys and the corresponding bean instances as values
	 * @throws BeansException if a bean could not be created
	 * @since 1.1.2
	 * @see FactoryBean#getObjectType
	 * @see BeanFactoryUtils#beansOfTypeIncludingAncestors(ListableBeanFactory, Class)
	 */
	<T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException;

	/**
	 * Return the bean instances that match the given object type (including
	 * subclasses), judging from either bean definitions or the value of
	 * {@code getObjectType} in the case of FactoryBeans.
	 * <p><b>NOTE: This method introspects top-level beans only.</b> It does <i>not</i>
	 * check nested beans which might match the specified type as well.
	 * <p>Does consider objects created by FactoryBeans if the "allowEagerInit" flag is set,
	 * which means that FactoryBeans will get initialized. If the object created by the
	 * FactoryBean doesn't match, the raw FactoryBean itself will be matched against the
	 * type. If "allowEagerInit" is not set, only raw FactoryBeans will be checked
	 * (which doesn't require initialization of each FactoryBean).
	 * <p>Does not consider any hierarchy this factory may participate in.
	 * Use BeanFactoryUtils' {@code beansOfTypeIncludingAncestors}
	 * to include beans in ancestor factories too.
	 * <p>Note: Does <i>not</i> ignore singleton beans that have been registered
	 * by other means than bean definitions.
	 * <p>The Map returned by this method should always return bean names and
	 * corresponding bean instances <i>in the order of definition</i> in the
	 * backend configuration, as far as possible.
	 * @param type the class or interface to match, or {@code null} for all concrete beans
	 * @param includeNonSingletons whether to include prototype or scoped beans too
	 * or just singletons (also applies to FactoryBeans)
	 * @param allowEagerInit whether to initialize <i>lazy-init singletons</i> and
	 * <i>objects created by FactoryBeans</i> (or by factory methods with a
	 * "factory-bean" reference) for the type check. Note that FactoryBeans need to be
	 * eagerly initialized to determine their type: So be aware that passing in "true"
	 * for this flag will initialize FactoryBeans and "factory-bean" references.
	 * @return a Map with the matching beans, containing the bean names as
	 * keys and the corresponding bean instances as values
	 * @throws BeansException if a bean could not be created
	 * @see FactoryBean#getObjectType
	 * @see BeanFactoryUtils#beansOfTypeIncludingAncestors(ListableBeanFactory, Class, boolean, boolean)
	 */
	<T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
			throws BeansException;

	/**
	 * Find all names of beans which are annotated with the supplied {@link Annotation}
	 * type, without creating corresponding bean instances yet.
	 * <p>Note that this method considers objects created by FactoryBeans, which means
	 * that FactoryBeans will get initialized in order to determine their object type.
	 * @param annotationType the type of annotation to look for
	 * @return the names of all matching beans
	 * @since 4.0
	 * @see #findAnnotationOnBean
	 */
	String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);

	/**
	 * Find all beans which are annotated with the supplied {@link Annotation} type,
	 * returning a Map of bean names with corresponding bean instances.
	 * <p>Note that this method considers objects created by FactoryBeans, which means
	 * that FactoryBeans will get initialized in order to determine their object type.
	 * @param annotationType the type of annotation to look for
	 * @return a Map with the matching beans, containing the bean names as
	 * keys and the corresponding bean instances as values
	 * @throws BeansException if a bean could not be created
	 * @since 3.0
	 * @see #findAnnotationOnBean
	 */
	Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException;

	/**
	 * Find an {@link Annotation} of {@code annotationType} on the specified bean,
	 * traversing its interfaces and super classes if no annotation can be found on
	 * the given class itself.
	 * @param beanName the name of the bean to look for annotations on
	 * @param annotationType the type of annotation to look for
	 * @return the annotation of the given type if found, or {@code null} otherwise
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 3.0
	 * @see #getBeanNamesForAnnotation
	 * @see #getBeansWithAnnotation
	 */
	@Nullable
	<A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
			throws NoSuchBeanDefinitionException;

}
