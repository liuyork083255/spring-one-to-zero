package liu.york.spring.otz;

/**
 * 对于最不普通的 ClassPathXmlApplicationContext 类是不具备扫描 bean 功能的
 * 也就是说 new ClassPathXmlApplicationContext("spring-config.xml") 只能注册 xml 文件中注册的 bean
 * 当然这是默认情况
 *
 * spring 要完成自动扫描 bean 功能其实是通过一些列的 BeanFactoryPostProcessor
 *  1 如果 ClassPathXmlApplicationContext 想要实现扫描功能，那么必须在 xml 文件中添加 <context:component-scan base-package="package" />
 *    这样spring在解析的时候才会自动注入这一系列的 BeanFactoryPostProcessor
 *  2 如果 AnnotationConfigApplicationContext 启动，其实默认会自动添加，在其父类中委托给 AnnotationConfigUtils.registerAnnotationConfigProcessors 方法
 *
 *
 */
public class BeanScanS {
}
