package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

/**
 * 变换操作符
 */
public class TransformActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
//        map();
//        flatMap();
//        concatMap();
//        flatMapIterable();
//        buffer();
        groupBy();
    }

    //分组发射 结果为groupBy:韦一笑---A,groupBy:殷梨亭---A,groupBy:宋青书---A,groupBy:张三丰---SS,groupBy:张无忌---SS,groupBy:周芷若---S,groupBy:宋远桥---S,groupBy:鹤笔翁---S
    private void groupBy() {
        Swordsman s1 = new Swordsman("韦一笑", "A");
        Swordsman s2 = new Swordsman("张三丰", "SS");
        Swordsman s3 = new Swordsman("周芷若", "S");
        Swordsman s4 = new Swordsman("宋远桥", "S");
        Swordsman s5 = new Swordsman("殷梨亭", "A");
        Swordsman s6 = new Swordsman("张无忌", "SS");
        Swordsman s7 = new Swordsman("鹤笔翁", "S");
        Swordsman s8 = new Swordsman("宋青书", "A");

        //Observable.just添加数据，通过groupBy分组（按照Func1里面call的返回值分），分组后返回
        Observable<GroupedObservable<String, Swordsman>> GroupedObservable = Observable.just(s1, s2, s3, s4, s5, s6, s7, s8)
                .groupBy(new Func1<Swordsman, String>() {
            @Override
            public String call(Swordsman swordsman) {
                return swordsman.getLevel();
            }
        });
        //concat将多个Observable发射的数据进行合并发射，严格按照顺序发射，前一个不发完，就不会发射后一个Observable
        Observable.concat(GroupedObservable).subscribe(new Action1<Swordsman>() {
            @Override
            public void call(Swordsman swordsman) {
                Log.d(TAG, "groupBy:" + swordsman.getName() + "---" + swordsman.getLevel());
            }
        });


    }


    //将源Observable变换成一个新Observable,新的每次发射一组列表值，类似的有window，不过window是发送Observable而不是列表值，下面的结果是1，2，---，3，4，---，5，6，---
    private void buffer() {
        //添加数据
        Observable.just(1, 2, 3, 4, 5, 6)
                //包装成新的Observable,新的里面是2个数一组的List<Integer>，知道发完，这里刚好发3次
                .buffer(2)
                .subscribe(new Action1<List<Integer>>() {
                    //里面数据已经被buffer变为List数组了，1，2一组，3，4一组5，6一组
                    @Override
                    public void call(List<Integer> integers) {
                        for (Integer i : integers) {
                            Log.d(TAG, "buffer:" + i);
                        }
                        Log.d(TAG, "-----------------");
                    }
                });
    }

    //可以把数据包装成Iterable（子类有Collection<E>（子类有List<E>））,在Iterable中就可以对数据进行处理再返回，下面结果是2,3,3,4,4,5
    private void flatMapIterable() {
        //先添加数据，再把数据
        Observable.just(1, 2, 3).flatMapIterable(new Func1<Integer, Iterable<Integer>>() {
            //可以添加多个数据后返回Iterable<Integer>类，一共3个数1，2，3，第一次返回2,3的Iterable,第二次返回3，4，第三次返回4,5
            @Override
            public Iterable<Integer> call(Integer s) {
                List<Integer> mlist = new ArrayList<Integer>();
                mlist.add(s + 1);
                mlist.add(s + 2);
                return mlist;
            }
            //发射数据
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "flatMapIterable:" + integer);
            }
        });
    }

    //concatMap功能跟下面的flatMap()类似，但都是顺序发射，他是把发射的值连续在一起，而不是合并他们
    private void concatMap() {
        final String Host = "http://blog.csdn.net/";
        List<String> mlist = new ArrayList<>();
        mlist.add("itachi85");
        mlist.add("itachi86");
        mlist.add("itachi87");
        mlist.add("itachi88");
        Observable.from(mlist).concatMap(new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String s) {
                return Observable.just(Host + s);
            }
        }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "concatMap:" + s);
            }
        });
    }

    //通过flatMap把数据集合，变换为Observable集合,通过cast转化为指定类型，再发射(允许交叉，可能顺序会变)，下面结果是http://blog.csdn.net/itachi85,http://blog.csdn.net/itachi86......
    private void flatMap() {
        final String Host = "http://blog.csdn.net/";
        List<String> mlist = new ArrayList<>();
        mlist.add("itachi85");
        mlist.add("itachi86");
        mlist.add("itachi87");
        mlist.add("itachi88");
        //通过from添加数据，通过flatMap转化为Observable集合
        Observable.from(mlist).flatMap(new Func1<String, Observable<?>>() {
            @Override
            //下面是把数据添加为 Host + s再转换Observable返回
            public Observable<?> call(String s) {
                return Observable.just(Host + s);
            }
            //通过cast转化为String类，再发射集合String类，下面再通过call处理
        }).cast(String.class).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "flatMap:" + s);
            }
        });
    }

    //可以转化数据，可以改变类型，之后再发射数据，下面结果是map:http://blog.csdn.net/itachi85
    private void map() {
        final String Host = "http://blog.csdn.net/";
        //map,把Observable转化成新的Observable再发送，这里map是把just要发送的数据，转化一下，再返回，new Func1<1, 2>() 1是下面call的参数类型，2是call的返回类型
        Observable.just("itachi85").map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                return Host + s;
            }
            //这里如果没有加map就是执行just("itachi85"),被map转化成了Object类，并且数据变为Host + s
        }).subscribe(new Action1<Object>() {
            @Override
            public void call(Object s) {
                Log.d(TAG, "map:" + s);
            }
        });
    }
}
