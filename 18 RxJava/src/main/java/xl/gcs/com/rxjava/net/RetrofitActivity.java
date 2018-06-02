package xl.gcs.com.rxjava.net;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import xl.gcs.com.rxjava.IpServiceForPost;
import xl.gcs.com.rxjava.R;
import xl.gcs.com.rxjava.model.HttpResult;
import xl.gcs.com.rxjava.model.IpData;

public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    private Subscription subscription;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
    }
    @Override
    protected void onResume() {
        super.onResume();
        postIpInformation("59.108.54.37");
    }
    @Override
    public void onStop() {
        super.onStop();
        //先打印看看取消了没有，如果没有执行完，和没有取消，结果应该是false
        Log.d(TAG, "onStop:" + subscription.isUnsubscribed());
        //如果不为null,如果还没取消订阅（事件onCompleted和onError也会自动取消），相当于这里是，如果还没执行完，也还没取消
        if (subscription != null && !subscription.isUnsubscribed()) {
            //这里是通过下面方法取消掉，如果是单纯的Retrofit就是通过call.cancel取消的
            subscription.unsubscribe();
        }
        //取消掉了，结果肯定是true
        Log.d(TAG, "onStop:" + subscription.isUnsubscribed());

        //可以通过compositeSubscription来统一管理subscription，
//        compositeSubscription.add(subscription);
        //取消里面的全部请求
//        compositeSubscription.unsubscribe();
    }
    private void postIpInformation(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                //传入基本地址，一会还要拼接上post里面的地址字段
                .baseUrl(url)
                //增加返回值为Gson的支持
                .addConverterFactory(GsonConverterFactory.create())
                //添加返回适配，这样就可以用接收之前定义的post接口，返回Observable数据了,没有这行，那接口应该返回call数据
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //获取到之前定义的post接口
        IpServiceForPost ipService = retrofit.create(IpServiceForPost.class);
        //传入ip参数，指定被观察者再io线程中执行，指定观察者在main线程中执行，返回Subscription,就可以取消订阅，或查询是否已经取消
        subscription=ipService.getIpMsg(ip)
                //指定被观察者在io线程上执行，指定观察者在主线程中执行
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                //经过Gson解析，传到里面的参数就是HttpResult
                .subscribe(new Subscriber<HttpResult<IpData>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<IpData> ipDataHttpResult) {
                //这里的数据已经Gson解析过了，下面可以直接用
                IpData data=ipDataHttpResult.getData();
                Toast.makeText(getApplicationContext(), data.getCountry(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
