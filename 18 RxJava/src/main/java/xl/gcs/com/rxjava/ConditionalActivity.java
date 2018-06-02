package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 条件和布尔操作符
 */
public class ConditionalActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditional);
//        all();
//        contains();
//        isEmpty();

        //下面是条件操作符
//        amb();
        defaultIfEmpty();
    }

    //发射原始Observable数据，如果原始Observable没有发射数据，就发射一个默认数据,结果是先发射 3，再调用onCompleted
    private void defaultIfEmpty() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                //这里没有发射数据，而直接调用完成
                subscriber.onCompleted();
            }
            //由于上面没发数据，所以通过defaultIfEmpty发射默认数据 3
        }).defaultIfEmpty(3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
            }
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ");
            }
            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "defaultIfEmpty:"+integer);
            }
        });
    }

    private void amb() {
        //delay是延迟2秒发射，amb是给定两个或多个Observable，只发射首先发射数据的Observable，或通知的那个Observable，
        // 这里因为第一个延迟2秒了，所以就只会发射第二个Observable的数据，第一个不管他，结果明显是4，5，6
        Observable.amb(Observable.just(1,2,3).delay(2, TimeUnit.SECONDS),Observable.just(4,5,6))
                .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "amb:"+integer);
            }
        });
    }

    //看下是否发射过数据，这里明显可以正常发射数据的，就返回false(不空), 运行结果只有一个false
    private void isEmpty() {
        Observable.just(1,2,3).isEmpty().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d(TAG, "isEmpty:"+aBoolean);
            }
        });
    }

    //有包含就返回true,全部运行完都没有包含就返回false,这里结果就是只有一个true
    private void contains() {
        Observable.just(1,2,3).contains(3).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.d(TAG, "contains:"+aBoolean);
            }
        });
    }

    //再里面定条件，如果全部满足就返回true,否则就返回false，接下来也不执行
    private void all() {
        Observable.just(1,2,3)
                //对源Observable发射的所有数据进行判断，最终返回的结果就是这个判断结果
                .all(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        Log.d(TAG, "call:"+integer);
                        //返回是不是全部小于2，如果全都运行完了，都是true,就最后返回true,否则，就返回false，并不继续执行
                        return integer<2;
                    }
                }).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:"+e.getMessage());
            }
            @Override
            public void onNext(Boolean aBoolean) {
                Log.d(TAG, "onNext:"+aBoolean);
            }
        });
    }
}
