package xl.gcs.com.rxjava.bus;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;

/**
 * Created by xianglei on 2018/5/3.
 */

public class BasicRxJava {

    public static void basicComeTrue() {

        //创建观察者，他会监控被观察者添加的mSubscriber.onNext(),mSubscriber.onCompleted(),前3个方法必须实现，onStart()可选
        //当被观察者subscribe后，如果有就会先执行onStart(),再执行所有的onNext(),最后onCompleted(),出错就onError()
        final Subscriber mSubscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "2onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "2onError: ");
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "2onNext: " + (String) o);
            }

            @Override
            public void onStart() {
                Log.d(TAG, "2onStart: ");
            }
        };

        final Observer<String> mObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "1onCompleted: ");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "1onError: ");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "1onNext: " + s);
            }
        };

        //创建被观察者 call方法里选择哪个观察者添加观察数据，onNext添加完数据后，需要onCompleted(),
        // 如果全是有指定被观察者添加的数据（比如mObserver.onNext("ddd")），下面只需要执行订阅，括号里不用传参数，传了也没用，
        // 哪个观察者添加了数据，都会运行，如果没有指定，只是call里参数subscriber来添加的，就要指定观察者了
        Observable mObservable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("杨影");
                subscriber.onNext("月儿");
                subscriber.onCompleted();
            }
        });

        //通过这两种方式添加数据，下面也能执行，由于没有指明哪个观察者添加数据，那就必须在开始订阅的时候传参数，告诉他由哪个观察者订阅
//        Observable mObservable = Observable.just("987", "789");
        String[] words = {"567", "765"};
//        Observable mObservable = Observable.from(words);
        //开始订阅，相当于观察者开始查看数据会依次从onStart(),onNext(),onCompleted()开始运行,括号里的参数作用是，如果没有指明添加哪个观察者数据，就让括号里的观察者订阅
        mObservable.subscribe(mSubscriber);





        //另一种方式来订阅,相当于把类分解开来，下面先是订阅String参数的数据
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "3onNext:" + s);
            }
        };
        //再订阅Throwable的数据，就是出错会传的
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(TAG, "3onError");
            }
        };
        //接着订阅空参数
        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "3onCompletedAction");
            }
        };
        //这种方式灵活性更大，一个订阅者只有一个方法，下面实现了3个方法，也可以实现一个或两个方法,下面连续3行也能执行，会依次订阅
//        mObservable.subscribe(onNextAction, onErrorAction, onCompletedAction);
//        mObservable.subscribe(onNextAction);
//        mObservable.subscribe(onNextAction, onErrorAction);

    }
}
