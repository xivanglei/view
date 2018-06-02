package xl.gcs.com.myapplication.dagger2.module;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import xl.gcs.com.myapplication.dagger2.annotation.ApplicationScope;

/**
 * Created by xianglei on 2018/6/1.
 */

//将Module标注在类上，用来告诉Component,可以从这个类中获取依赖对象，也就是Gson类，Module标注的类其实就是一个工厂，用来生成各种类，@Provides就是用来生成这些类的实例的
@Module
public class GsonModule {
    //@Provides标记在方法上，表示可以通过这个方法来获取依赖对象的实例。
    @Provides
    //@Singleton表示可以使用单例，但还要再Dagger2ActivityComponent的@Component上加@Singleton配合
//    @Singleton
    //@ApplicationScope里面有@Scope注解，就可以用这个注释表明全局单例，还要在Dagger2ActivityComponent里用@ApplicationScope注解
    @ApplicationScope
    public Gson provideGson() {
        return new Gson();
    }
}
