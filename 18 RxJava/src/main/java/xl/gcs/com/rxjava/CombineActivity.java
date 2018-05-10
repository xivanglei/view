package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * 组合操作符
 */
public class CombineActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);
//        startWith();
//        merge();
//        concat();
//        zip();
        combineLatest();

    }

    //combineLatest跟zip类似，但有区别，测试结果是第一组发完了，取最后一个数跟第二组数据合并，通过call方法返回数据再发射
    //下面结果是4a,4b,4c,4d,4e，发射次数跟第二组数量有关，第一组取值跟第一组最后一位数有关
    private void combineLatest() {
        Observable<Integer> obs1 =Observable.just(1,2,3, 4);
        Observable<String> obs2 =Observable.just("a","b","c", "d", "e");
        Observable.combineLatest(obs1, obs2, new Func2<Integer, String, String>() {
            @Override
            public String call(Integer integer, String s) {
                return integer+s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "combineLastest:"+s);
            }
        });

    }

    //zip合并多个Observable发射出的数据项，根据指定的方法变换他们，并发射一个新值,没得合并也不会单发，比如下面多个4，就不会发，下面结果是1a，2b，3c
    private void zip() {
        Observable<Integer> obs1 =Observable.just(1,2,3, 4);
        Observable<String> obs2 =Observable.just("a","b","c");
        //Func2<1,2,3>，1是下面call参1，2是参2，3是返回的参数，这里是通过zip发射call里的返回值（String）
        Observable.zip(obs1, obs2, new Func2<Integer, String, String>() {

            @Override
            public String call(Integer integer, String s) {
                return integer+s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "zip:"+s);
            }
        });
    }

    //concat将多个Observable发射的数据进行合并发射，严格按照顺序发射，前一个不发完，就不会发射后一个Observable，下面结果是1，2，3，4，5，6
    private void concat() {
        Observable<Integer> obs1 =Observable.just(1,2,3).subscribeOn(Schedulers.io());
        Observable<Integer> obs2 =Observable.just(4,5,6);
        Observable.concat(obs1,obs2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "concat:"+integer);
            }
        });
    }

    //merge可以把多个Observable合并到一个Observable中进行发射，可能会让合并的Observable发射的数据交错，下面结果是4,5,6,1,2,3
    private void merge() {
        //这边通过subscribeOn让obs1的顺序靠后了
        Observable<Integer> obs1 =Observable.just(1,2,3).subscribeOn(Schedulers.io());
        Observable<Integer> obs2 =Observable.just(4,5,6);
        Observable.merge(obs1,obs2).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "merge:"+integer);
            }
        });
    }

    //just添加数据，在发射之前可以通过startWith插入数据，下面结果是1，2，3，4，5
    private void startWith() {
        Observable.just(3, 4, 5).startWith(1, 2)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "startWith:"+integer);
                    }
                });
    }
}
