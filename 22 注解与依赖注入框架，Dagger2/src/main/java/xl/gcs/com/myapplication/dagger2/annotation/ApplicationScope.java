package xl.gcs.com.myapplication.dagger2.annotation;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2016/12/20 0020.
 */
//使用@Scope注解，可以让ApplicationScope注解适用于全局单例
@Scope
//运行时注解
@Retention(RUNTIME)
public @interface ApplicationScope {
}
