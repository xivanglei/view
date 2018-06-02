package xl.gcs.com.myapplication.dagger2.component;

import javax.inject.Singleton;

import dagger.Component;
import xl.gcs.com.myapplication.dagger2.Dagger2Activity;
import xl.gcs.com.myapplication.dagger2.SecondActivity;
import xl.gcs.com.myapplication.dagger2.annotation.ApplicationScope;
import xl.gcs.com.myapplication.dagger2.module.EngineModule;
import xl.gcs.com.myapplication.dagger2.module.GsonModule;

/**
 * Created by xianglei on 2018/6/1.
 */
//单例注解，只要注明过@Singleton的类（比如GsonModule里的Gson类），都是按照单例实例化，但只保证每个使用类里的Gson类是单例，在SecondActivity里又是不同地址的单例
//@Singleton
    //@ApplicationScope里面有@Scope注解，就可以用这个注释表明全局单例
    @ApplicationScope
//用@Component来注解完成依赖注入，建议类名为 调用方目标类名+Component，编译后Dagger2就会为我们生成名为Dagger+下面类名的辅助类
    //辅助类就能让Dagger2Activity通过注解来实例化类，具体使用方法看Dagger2Activity
    //括号里表示指定Module，配合GsonModule类里的标注，就可以在使用类里引用了，括号里可以包含多个Module
    //dependencies表示引入其他Component，这里是引入SwordsmanComponent
@Component(modules = {GsonModule.class, EngineModule.class}, dependencies = SwordsmanComponent.class)
public interface Dagger2ActivityComponent {
    //这里的参数说明要在哪个类里实例化
    void inject(Dagger2Activity activity);
    void inject(SecondActivity activity);
}
