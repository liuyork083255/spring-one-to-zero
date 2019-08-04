package liu.york.spring.otz;

/**
 *  <bean /> 标签中可以定义的属性
 *  	class					 ： 类的全限定名
 * 		name					 ： 可指定 id、name(用逗号、分号、空格分隔)
 * 		scope					 ： 作用域
 * 		constructor arguments	 ： 指定构造参数
 * 		properties				 ： 设置属性的值
 * 		autowiring mode			 ： no(默认值)、byName、byType、 constructor
 * 		lazy-initialization mode ： 是否懒加载(如果被非懒加载的bean依赖了那么其实也就不能懒加载了)
 * 		initialization method	 ： bean 属性设置完成后，会调用这个方法
 * 		destruction method		 ： bean 销毁后的回调方法
 *
 */
public class BeanPropertyS {
}
