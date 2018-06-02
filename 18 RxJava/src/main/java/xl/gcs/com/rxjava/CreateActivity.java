package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * 创建操作符
 */
public class CreateActivity extends AppCompatActivity {
 private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        interval();
//        range();
//        repeat();
    }

    //相当于多次range()，也只能Integer,下面结果是0,1,2,0,1,2
    private void repeat() {
        Observable.range(0, 3)
                //循环次数
                .repeat(2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "repeat:" +integer.intValue());
                    }
                });
    }

    //可以代替for循环,好像不能打断，下面结果是 0，1，2，3，4
    private void range() {
        //从 0（不能小于0） 开始发送 5 个数字
        Observable.range(0, 5)
                //这里必须是Integer类
                .subscribe(new Action1<Integer>() {
                    //这里可以自己设定逻辑，像for循环一样
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "range:" + integer.intValue());
                    }
                });
    }

    //定时发射,从0开始发射，一直无限往上加，活动销毁也还继续加，但把程序退出就没了
    private void interval() {
        //每隔3（参1）秒（参2,也可以分(MINUTES)时（HOURS））
        Observable.interval(1, TimeUnit.SECONDS)
                //这个方法必须要是 Action1 Long类型，不能String 或Integer类型，并且每次发射都会让long+1
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long mlong) {
                        Log.d(TAG, "interval:" + mlong.intValue());
                    }
                });
    }


}
