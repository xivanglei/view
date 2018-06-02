package xl.gcs.com.myapplication.dagger2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.Lazy;
import xl.gcs.com.myapplication.R;
import xl.gcs.com.myapplication.dagger2.component.DaggerDagger2ActivityComponent;
import xl.gcs.com.myapplication.dagger2.model.Swordsman;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG="Dagger2";
    @Inject
    Gson gson;
    @Inject
    Gson gson1;
    //Swordsman是通过Dagger2ActivityComponent里引用别的Component
    @Inject
    Swordsman mSwordsman;
    //懒加载模式，在@Inject的时候不初始化，而是使用的时候，调用get方法来获取实例
    @Inject
    Lazy<Swordsman> swordsmanLazy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        App.get(SecondActivity.this).getActivityComponent().inject(this);
        //因为使用了全局单例，所以gson的地址跟Dagger2Activity里的gson地址一样的
        Log.d(TAG,gson.hashCode()+"-----"+gson1.hashCode());
        String sd = mSwordsman.fighting();
        Log.d(TAG, "swordsman---" + sd);
        //懒加载模式初始化类，调用get才开始获取实例
        Swordsman  swordsman=swordsmanLazy.get();
        String sd1=swordsman.fighting();
        Log.d(TAG, "lazy---" + sd1);
    }
}
