package xl.gcs.com.okhttp;

import android.content.Context;
import android.os.Handler;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class OkHttpEngine {
    private static volatile  OkHttpEngine mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    public static OkHttpEngine getInstance(Context context) {
        if (mInstance == null) {
            //同步类，本身就是单例，避免被开线程的时候，被两个方法都调用new
            synchronized (OkHttpEngine.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpEngine(context);
                }
            }
        }
        return mInstance;
    }

    private OkHttpEngine(Context context) {
        //缓存地址
        File sdcache = context.getExternalCacheDir();
        //缓存容量
        int cacheSize = 10 * 1024 * 1024;
        //客户端
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //连接超时
                .connectTimeout(15, TimeUnit.SECONDS)
                //传参超时
                .writeTimeout(20, TimeUnit.SECONDS)
                //返回数据超时
                .readTimeout(20, TimeUnit.SECONDS)
                //缓存地址
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
         mOkHttpClient=builder.build();
         mHandler = new Handler();
    }


    /**
     * 异步get请求
     * @param url
     * @param callback
     */
    public void getAsynHttp(String url, ResultCallback callback) {

        //请求类，传入url
        final Request request = new Request.Builder()
                .url(url)
                .build();
        //发送请求类，通过客户端的newCall传入Request
        Call call = mOkHttpClient.newCall(request);
        //通过下面方法加入请求队列，传入回调接口
        dealResult(call, callback);
    }


    private void dealResult(Call call, final ResultCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //如果失败，通过下面让handler线程操作,传入回调接口
                sendFailedCallback(call.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessCallback(response.body().string(), callback);
            }

            private void sendSuccessCallback(final String str, final ResultCallback callback) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            try {
                                callback.onResponse(str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            private void sendFailedCallback(final Request request, final Exception e, final ResultCallback callback) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onError(request, e);
                    }
                });
            }

        });
    }
}
