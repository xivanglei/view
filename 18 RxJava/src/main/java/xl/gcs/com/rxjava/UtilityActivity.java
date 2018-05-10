package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * 辅助/错误操作符
 */
public class UtilityActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
//        delay();
//        doOnNext();
//        subscribeOn();
        timeout();


    }

    //timeout可以让过了指定时间没有发射任何数据的Observable以onError通知来终止，或执行一个备用的Observable,这里是执行备用的Observable,timeout还有很多变体方式
    private void timeout() {
        Observable<Integer> obs = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for(int i=0;i<4;i++){
                    try {
                        Thread.sleep(i * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
            //timeout设置最长时间是200毫秒，这时间内如果没发数据，就执行后面的备用Observable
        }).timeout(200,TimeUnit.MILLISECONDS,Observable.just(10,11));
        obs.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "timeout:" + integer);
            }
        });
    }

    //subscribeOn,observeOn用于指定线程
    private void subscribeOn() {
        Observable<Integer> obs= Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Log.d(TAG,"Observable:" + Thread.currentThread().getName());
                subscriber.onNext(1);
                subscriber.onCompleted();
            }
        });
        //subscribeOn用于指定Observable自身在哪个线程上运行，这里是新开一条线程，observeOn用于指定观察者Observer在哪个线程运行，这里是主线程，一般都是主线程，方便修改UI
        obs.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG,"Observer:" + Thread.currentThread().getName());
            }
        });
    }

    //Do系列，为原始Observable的生命周期时间注册一个回调，当Observable的某个事件发生就会调用这些回调
    //doOnEach:每次发射数据就会调用，包括onNext,onError,onCompleted;doOnNext:执行onNext时调用;doOnSubscribe:当被观察者订阅时调用;
    // doOnUnSubscribe:当观察者取消订阅时调用，Observable通过onError或onCompleted结束时会取消所有Subscriber的订阅;
    // doOnCompleted:正常调用onCompleted时调用;doOnError:异常终止时调用;doOnTerminate:终止前调用，不管怎么终止;finallyDo:终止后调用，不管怎么终止
    //doOnNext执行onNext时的回调，下面结果是call:1,onNext:1,call:2,onNext:2,onCompleted
    private void doOnNext() {
        Observable.just(1,2)
                //注册一个回调
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer item) {
                        Log.d(TAG,"call:" + item);

                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                Log.d(TAG,"onNext:" + item);
            }
            @Override
            public void onError(Throwable error) {
                Log.d(TAG,"Error:" + error.getMessage());
            }
            @Override
            public void onCompleted() {
                Log.d(TAG,"onCompleted");
            }
        });

    }

    //delay让Observable在发射每项数据之前都暂停一段指定的时间段，下面结果是 delay:2
    private void delay() {
        Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                //先截取现在的秒数
                Long currentTime=System.currentTimeMillis()/1000;
                //发射数据
                subscriber.onNext(currentTime);
            }
            //delay延迟2秒发射
        }).delay(2,TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                //参1是传进来的2秒前的数，结果是现在的时间秒数-刚刚的时间
                Log.d(TAG, "delay:"+(System.currentTimeMillis()/1000-aLong));
            }
        });
    }
}
