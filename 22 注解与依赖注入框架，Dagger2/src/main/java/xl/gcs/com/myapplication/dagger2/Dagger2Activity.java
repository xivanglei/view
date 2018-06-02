package xl.gcs.com.myapplication.dagger2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xl.gcs.com.myapplication.MainActivity;
import xl.gcs.com.myapplication.R;
import xl.gcs.com.myapplication.dagger2.component.DaggerDagger2ActivityComponent;
import xl.gcs.com.myapplication.dagger2.model.Car;
import xl.gcs.com.myapplication.dagger2.model.Man;
import xl.gcs.com.myapplication.dagger2.model.Watch;

public class Dagger2Activity extends AppCompatActivity {


    public static final String TAG = "Dagger2Activity";
    //用@Inject来标记需要注入的属性，对应着Watch类的无参构造的Inject注解
    @Inject
    Watch mWatch;
    //实例依赖的类，由于不能在类里标注@Inject，所以要新建一个类GsonModule，在里面标注，还要在Dagger2ActivityComponent里@Component(modules = GsonModule.class)
    @Inject
    Gson mGson;
    //使用了单例，这里试一下
    @Inject
    Gson mGson1;
    //实例有参构造，但有参构造需要抽象的类，也用类似上面的方法，但在构建EngineModule的时候返回子类，再在Dagger2ActivityComponent里@Component(modules = EngineModule.class)
    @Inject
    Car car;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2);
        ButterKnife.bind(this);
        //接口类已经用@Component注解了，所以会生成Dagger+接口类名的辅助类，就可以使用辅助类了，下面相当于无参实例化所注解的全部类
//        DaggerDagger2ActivityComponent.create().inject(this);
        //用App里获取ActivityComponent实例，就不用每次create了
        App.get(this).getActivityComponent().inject(this);
        //上面已经实例化了，这里就可以使用方法了
        mWatch.work();
        String jsonData = "{'name':'zhangwuji', 'age':20}";
        Man man = mGson.fromJson(jsonData, Man.class);
        Log.d(TAG, "name----" + man.getName());
        String str = car.run();
        Log.d(TAG, "car---" + str);
        //使用了全局单例，所以地址就是一样的了
        Log.d(TAG, mGson.hashCode() + "--------" + mGson1.hashCode());

    }

    @OnClick(R.id.bt_jump)
    public void startSecondActivity() {
        startActivity(new Intent(this, SecondActivity.class));
    }
}
