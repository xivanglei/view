package xl.gcs.com.rxjava.net;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xl.gcs.com.rxjava.R;

public class OkhttpActivity extends AppCompatActivity {
    private static final String TAG="RxJava";
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

    }

    @Override
    protected void onResume() {
        super.onResume();
        postAsyncHttp("59.108.54.37");
    }

    private void postAsyncHttp(String size) {
        //先获取下面的被观察者，里面会发送结果出来
        getObservable(size)
                //指定被观察者再io线程上运行，观察者在主线程中执行，然后指定观察者
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
            }
            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
                Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Observable<String> getObservable(final String ip) {
        //创建String类型的被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                //请求客户端
                mOkHttpClient = new OkHttpClient();
                //post请求的参数类
                RequestBody formBody = new FormBody.Builder()
                        .add("ip", ip)
                        .build();
                //请求类
                Request request = new Request.Builder()
                        .url("http://ip.taobao.com/service/getIpInfo.php")
                        .post(formBody)
                        .build();
                //加入请求，准备发送
                Call call = mOkHttpClient.newCall(request);
                //发送请求，加入回调
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //通过Observable发送错误
                        subscriber.onError(new Exception("error"));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        //通过Observable发送数据，并执行完毕回调
                        subscriber.onNext(str);
                        subscriber.onCompleted();
                    }
                });
            }
        });
        //返回被观察者
        return observable;
    }

}
