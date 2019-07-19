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

package org.springframework.context;

/**
 * A common interface defining methods for start/stop lifecycle control.
 * The typical use case for this is to control asynchronous processing.
 * <b>NOTE: This interface does not imply specific auto-startup semantics.
 * Consider implementing {@link SmartLifecycle} for that purpose.</b>
 *
 * <p>Can be implemented by both components (typically a Spring bean defined in a
 * Spring context) and containers  (typically a Spring {@link ApplicationContext}
 * itself). Containers will propagate start/stop signals to all components that
 * apply within each container, e.g. for a stop/restart scenario at runtime.
 *
 * <p>Can be used for direct invocations or for management operations via JMX.
 * In the latter case, the {@link org.springframework.jmx.export.MBeanExporter}
 * will typically be defined with an
 * {@link org.springframework.jmx.export.assembler.InterfaceBasedMBeanInfoAssembler},
 * restricting the visibility of activity-controlled components to the Lifecycle
 * interface.
 *
 * <p>Note that the present {@code Lifecycle} interface is only supported on
 * <b>top-level singleton beans</b>. On any other component, the {@code Lifecycle}
 * interface will remain undetected and hence ignored. Also, note that the extended
 * {@link SmartLifecycle} interface provides sophisticated integration with the
 * application context's startup and shutdown phases.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see SmartLifecycle
 * @see ConfigurableApplicationContext
 * @see org.springframework.jms.listener.AbstractMessageListenerContainer
 * @see org.springframework.scheduling.quartz.SchedulerFactoryBean
 *
 * otz:
 * 	Lifecycle 在开发中用的很少，原因是实现了这个接口的bean并不会在主动被回调，而是需要被动执行组件的相关方法才会被执行
 * 	如果考虑到这个原因，则可以使用 {@link SmartLifecycle}
 *
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
public interface Lifecycle {

	/**
	 * Start this component.
	 * <p>Should not throw an exception if the component is already running.
	 * <p>In the case of a container, this will propagate the start signal to all
	 * components that apply.
	 * @see SmartLifecycle#isAutoStartup()
	 *
	 * 在容器的情况下，这会将 开始信号 传播到应用的所有组件中去
	 * 前提是需要主动调用容器该方法
	 */
	void start();

	/**
	 * Stop this component, typically in a synchronous fashion, such that the component is
	 * fully stopped upon return of this method. Consider implementing {@link SmartLifecycle}
	 * and its {@code stop(Runnable)} variant when asynchronous stop behavior is necessary.
	 * <p>Note that this stop notification is not guaranteed to come before destruction:
	 * On regular shutdown, {@code Lifecycle} beans will first receive a stop notification
	 * before the general destruction callbacks are being propagated; however, on hot
	 * refresh during a context's lifetime or on aborted refresh attempts, a given bean's
	 * destroy method will be called without any consideration of stop signals upfront.
	 * <p>Should not throw an exception if the component is not running (not started yet).
	 * <p>In the case of a container, this will propagate the stop signal to all components
	 * that apply.
	 * @see SmartLifecycle#stop(Runnable)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	void stop();

	/**
	 * Check whether this component is currently running.
	 * <p>In the case of a container, this will return {@code true} only if <i>all</i>
	 * components that apply are currently running.
	 * @return whether the component is currently running
	 *
	 * 检查此组件是否正在运行。
	 * 		1. 只有该方法返回false时，start方法才会被执行。
	 * 		2. 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行。
	 */
	boolean isRunning();

}
