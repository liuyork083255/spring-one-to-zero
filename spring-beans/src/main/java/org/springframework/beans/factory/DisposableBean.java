/*
 * Copyright 2002-2018 the original author or authors.
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

/**
 * Interface to be implemented by beans that want to release resources on destruction.
 * A {@link BeanFactory} will invoke the destroy method on individual destruction of a
 * scoped bean. An {@link org.springframework.context.ApplicationContext} is supposed
 * to dispose all of its singletons on shutdown, driven by the application lifecycle.
 *
 * <p>A Spring-managed bean may also implement Java's {@link AutoCloseable} interface
 * for the same purpose. An alternative to implementing an interface is specifying a
 * custom destroy method, for example in an XML bean definition. For a list of all
 * bean lifecycle methods, see the {@link BeanFactory BeanFactory javadocs}.
 *
 * @author Juergen Hoeller
 * @since 12.08.2003
 * @see InitializingBean
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName()
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#destroySingletons()
 * @see org.springframework.context.ConfigurableApplicationContext#close()
 *
 * otz:
 * 	Lifecycle 在开发中用的很少，原因是实现了这个接口的bean并不会在主动被回调，而是需要被动执行组件的相关方法才会被执行
 * 	e.g.
 * 		一个类实现该接口 Lifecycle，spring容器启动后这些方法并不会被调用，需要手动获取上下文 context，然后调用 start stop
 * 		这个时候，spring 容器将在容器上下文中找出所有实现了 LifeCycle 及其子类接口的类，并一一调用它们实现的类
 * 	不过 spring是通过委托给生命周期处理器 LifecycleProcessor 来实现这一点的。
 *
 * 这里需要和 bean 的生命周期回调方法区分开
 * 	{@link org.springframework.beans.factory.InitializingBean}
 * 		初始化bean之后会调用这个接口方法 等价于{@link javax.annotation.PostConstruct}
 * 	{@link org.springframework.beans.factory.DisposableBean}
 * 		销毁bean之后会调用这个接口方法 等于于 {@link javax.annotation.PreDestroy}
 *
 * 初始化方法还可以通过 xml 配置方式指定
 * 		<bean id="" class="class" init-method="init3" destroy-method="destroy3"/>
 *
 * 所以针对实现了 注解、接口、xml配置 三种初始化，执行顺序如下：
 * 		1. @PostConstruct 注解方式
 * 		2. InitializingBean 实现接口方式
 * 		3. custom init() 自定义初始化方法方式
 *
 */
public interface DisposableBean {

	/**
	 * Invoked by the containing {@code BeanFactory} on destruction of a bean.
	 * @throws Exception in case of shutdown errors. Exceptions will get logged
	 * but not rethrown to allow other beans to release their resources as well.
	 */
	void destroy() throws Exception;

}
