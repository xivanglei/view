package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 错误处理操作符
 */
public class ErrorActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
//        onErrorReturn();
        retry();
    }

    //下面结果是0,0,0, onError:Throwable
    private void retry() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    if (i==1) {
                        //等于1就甩出错误
                        subscriber.onError(new Throwable("Throwable"));
                    }else {
                        subscriber.onNext(i);
                    }
                }
                subscriber.onCompleted();
            }
            //出错后，再给两次无错误的完成其数据序列，相当于第一次正确输出0，1的时候出错，就再运行次，还是0，再次0，接着就输出错误项了
        }).retry(2).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:" + e.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext:" + integer);
            }
        });
    }

    //结果是 0，1，2，6  如果出错就发射一个特殊的项，再调用观察者的onCompleted
    private void onErrorReturn() {
        //创建被观察者
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    if (i > 2) {
                        //大于2就甩出错误
                        subscriber.onError(new Throwable("Throwable"));
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
            //错误处理操作符，onErrorReturn是如果出错，就停止发射，然后返回下面的数据
        }).onErrorReturn(new Func1<Throwable, Integer>() {
            @Override
            public Integer call(Throwable throwable) {
                //这里是返回6
                return 6;
            }
            //指定观察者
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:" + e.getMessage());
            }
            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext:" + integer);
            }
        });
    }
}
