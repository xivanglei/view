package xl.gcs.com.myapplication.dagger2.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2016/12/19 0019.
 */
//@Qualifier来区分类，需要区分的类中就可以用@Diesel来区分了
@Qualifier
//表示运行时注解
@Retention(RUNTIME)
public @interface Diesel {
}
