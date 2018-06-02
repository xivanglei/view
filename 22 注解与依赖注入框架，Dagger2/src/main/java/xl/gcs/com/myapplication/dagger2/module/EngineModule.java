package xl.gcs.com.myapplication.dagger2.module;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import xl.gcs.com.myapplication.dagger2.annotation.Diesel;
import xl.gcs.com.myapplication.dagger2.annotation.Gasoline;
import xl.gcs.com.myapplication.dagger2.model.DieselEngine;
import xl.gcs.com.myapplication.dagger2.model.Engine;
import xl.gcs.com.myapplication.dagger2.model.GasolineEngine;

/**
 * Created by Administrator on 2016/12/18 0018.
 * 实例化抽象类的一种方式
 */

//将Module标注在类上，用来告诉Component,可以从这个类中获取依赖对象，也就是Engine类
@Module
public class EngineModule {
    //然而返回子类GasolineEngine类也可以
//    @Named("Gasoline")
    @Provides
    @Gasoline
    public Engine provideGasoline() {
        return new GasolineEngine();
    }

    //这里再提供一个子类给他，就会报错，因为系统不知道要实例化哪个类，不过可以用@Named或Qualifier来区分，再在使用这参数类的类里表明用哪个子类
    //@Named是括号里放标识字符来区分
    //而Qualifier需要建两个注解接口再用Qualifier来标注来区分，具体看annotation包里的Diesel和Gasoline类，下面还要配合这两个类里的标注
//    @Named("Diesel")
    @Provides
    @Diesel
    public Engine provideDiesel() {
        return new DieselEngine();
    }
}
