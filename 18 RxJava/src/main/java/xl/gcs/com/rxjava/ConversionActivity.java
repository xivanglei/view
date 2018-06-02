package xl.gcs.com.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 *转换操作符
 */
public class ConversionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion2);
//        toList();
//        toSortedList();
        toMap();
    }

    //区别toList在于多了排序，数字都好排序，如果是对象，就要实现Comparable接口，如果没有这接口，也可以使用toSortedList(Func2)变体
    private void toSortedList() {
        Observable.just(3,1,2).toSortedList().subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                for(int integer :integers){
                    Log.i("wangshu", "toSortedList:" + integer);
                }
            }
        });

    }

    //把所发射的数据整合成Map(默认是HashMap),然后发射这个Map，结果就是toMap:张三丰
    private void toMap() {
        Swordsman s1 = new Swordsman("韦一笑", "A");
        Swordsman s2 = new Swordsman("张三丰", "SS");
        Swordsman s3 = new Swordsman("周芷若", "S");
        //发射数据，通过toMap转换成Map，new Func1的第二项String返回的就是键值
        Observable.just(s1,s2,s3).toMap(new Func1<Swordsman, String>() {
            @Override
            public String call(Swordsman swordsman) {
                //返回String数值做键值
                return swordsman.getLevel();
            }
            //这里键就是刚刚返回的，值就是添加的Swordsman
        }).subscribe(new Action1<Map<String, Swordsman>>() {
            @Override
            public void call(Map<String, Swordsman> stringSwordsmanMap) {
                //这里只打印一个键为ss的值，结果就是toMap:张三丰
                Log.i("wangshu", "toMap:" + stringSwordsmanMap.get("SS").getName());
            }
        });
    }

    //把将发射多项数据且为每一项数据调用onNext方法的Observable发射的多项数据组合成一个List,然后调用一次onNext方法
    private void toList() {
        Observable.just(1,2,3)
                //相当于把1，2，3直接整合成list列表
                .toList()
                .subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> integers) {
                //通过for循环遍历这个 list列表
                for(int integer :integers){
                    Log.i("wangshu", "toList:" + integer);
                }
            }
        });
    }
}
