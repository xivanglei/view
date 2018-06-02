package xl.gcs.com.annotations.runtime;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2016/12/12 0012.
 */

//表示这个注解应该被JavaDoc工具记录
@Documented
//@Target是定义注解所修饰的对象范围，METHOD表示这注解应用于方法
@Target(METHOD)
//生命周期长度SOURCE < CLASS < RUNTIME, 如果要运行时去动态获取注解信息，就必须是RUNTIME,如果是编译时预处理，一般是CLASS，如果是做一些检查时的操作，可选SOURCE
@Retention(RUNTIME)

//通过@interface定义新的注解类型,接着就可以在程序中使用该注解@GET
public @interface GET {

    //注解只有成员变量，没有方法。以"无形参的方法"形式来声明，其"方法名"定义了该成员变量的名字，其返回值定义了该成员变量的类型。
    //default表示默认值，也可以没有默认值int age(); 可以有多个不同类型的成员变量
    String value() default "";
}