package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 过滤操作符
 */
public class FilterActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
//        filter();
//        elementAt();
//        distinct();
//        skip();
//        take();
//        ignoreElements();
//        throttleFirst();
        throttleWithTimeOut();


    }

    //间隔时间内如果没有新数据进来只发射最后一个数，如果有数据进来，就抛弃之前的数据，再重新记时
    private void throttleWithTimeOut() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                    //1毫秒添加一位数
                    int sleep = 100;
                    //如果是3的倍数
                    if (i % 3 == 0) {
                        //等待时间调整为5毫秒，主要是要超过下面的2毫秒，这样2毫秒内就不会有新数据送过去，他就能执行一次了
                        sleep = 500;
                    }
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }

            //两毫秒内只要有数据发过来，就会重新记时，相当于上面1毫秒一次发几百次都会被抛弃，知道等待超过2毫秒，就会发送最后一位数，之后每次接收到数据又会重新记时
            //所以，上面i % 3 == 0下面的sleep == 201与500效果一样，除非小于200那就会是一直发射一直被抛弃，直到最后一位数发完，等2毫秒，就发射最后一个数
    }).throttleWithTimeout(200,TimeUnit.MILLISECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "throttleWithTimeOut:"+integer);
            }
        });
    }

    //间隔时间内只发射第一个数
    private void throttleFirst() {
        //通过create创建被观察者，call里添加数据，1秒添加一次，下面执行结果是 0，3，6，9
       Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                  for(int i=0;i<11;i++){
                      subscriber.onNext(i);
                      try {
                          Thread.sleep(1000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  }
                subscriber.onCompleted();
            }
            //3秒发射一次数据，虽然3秒已经添加了3次数据了，但这里只发射所添加的第一个数据
        }).throttleFirst(3000, TimeUnit.MILLISECONDS).subscribe(new Action1<Integer>() {
            //观察者的执行逻辑
           @Override
           public void call(Integer integer) {
               Log.d(TAG, "throttleFirst:"+integer);
           }
       });
    }

    //ignoreElements是忽略源Observable产生的结果，只通知onCompleted和onError,下面结果是wangshu:onCompleted
    private void ignoreElements() {
        Observable.just(1, 2, 3, 4).ignoreElements().subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.i("wangshu", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("wangshu", "onError");
            }

            @Override
            public void onNext(Integer integer) {
                Log.i("wangshu", "onNext");
            }
        });
    }

    //take是只取n项 结果是 1，2
    private void take() {
        Observable.just(1, 2, 3, 4, 5, 6).take(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "take:" + integer);
            }
        });
    }

    //skip过滤掉n项，结果是3，4，5，6
    private void skip() {
        Observable.just(1, 2, 3, 4, 5, 6).skip(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "skip:" + integer);
            }
        });
    }

    //distinct只允许没发射过的数据通过，可以理解为去重复，结果是1，2，3，4类似的还有distinctUntilChanged,去掉连续重复的数据，不连续不去
    private void distinct() {
        Observable.just(1, 2, 2, 3, 4, 1).distinct().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "distinct:" + integer);
            }
        });
    }

    //elementAt返回指定位置的数据，0是第一位，2是第三位，下面结果是 第三位3，类似的有elementAtOrDefault(int, T),允许默认值
    private void elementAt() {
        Observable.just(1, 2, 3, 4).elementAt(2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "elementAt:" + integer);
            }
        });
    }

    //filter对源Observable产生的结果自定义规则进行过滤，下面>2才会提交给订阅者，结果是3，4
    private void filter() {
        //添加数据，通过filter过滤，过滤规则是Func1里的返回Boolean值
        Observable.just(1, 2, 3, 4).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                //大于2就返回true
                return integer > 2;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "filter:" + integer);
            }
        });
    }
}
