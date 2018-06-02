package xl.gcs.com.annotations.cls;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Administrator on 2016/12/12 0012.
 */
//定义是编译时注解
@Retention(CLASS)
//FIELD是修饰应用于成员变量
@Target(FIELD)
public @interface BindView {
    int value() default 1;
}

