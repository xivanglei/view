package xl.gcs.com.myapplication.dagger2;

import android.app.Application;
import android.content.Context;

import xl.gcs.com.myapplication.dagger2.component.Dagger2ActivityComponent;
import xl.gcs.com.myapplication.dagger2.component.DaggerDagger2ActivityComponent;
import xl.gcs.com.myapplication.dagger2.component.DaggerSwordsmanComponent;


/**
 * Created by Administrator on 2016/12/20 0020.
 * 使用全局单例，需要创建自定义App类提供Dagger2ActivityComponent实例
 */

public class App extends Application {
    //声明Dagger2ActivityComponent实例
    Dagger2ActivityComponent activityComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        //实例化Dagger2ActivityComponent并引入SwordsmanComponent,不引入的话就再builder()后面.build()就好了
        activityComponent= DaggerDagger2ActivityComponent.builder()
                .swordsmanComponent(DaggerSwordsmanComponent.builder().build()).build();
    }
    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }
    Dagger2ActivityComponent getActivityComponent(){
        //返回Dagger2ActivityComponent实例
        return activityComponent;
    }
}
