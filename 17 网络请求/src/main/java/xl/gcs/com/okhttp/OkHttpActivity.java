package xl.gcs.com.okhttp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xl.gcs.com.R;

public class OkHttpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG="OkHttp3";
    private OkHttpClient mOkHttpClient;
    private Button bt_send;
    private Button bt_postsend;
    private Button bt_sendfile;
    private Button bt_downfile;
    private Button bt_cancel;
    //定时线程池
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    //文件类型
    public static final MediaType MEDIA_TYPE_MARKDOWN
            //MediaType对象包含了三种信息：type 、subtype以及charset，一般将这些信息传入parse()方法中，这样就可以解析出MediaType对象，比如 "text/x-markdown; charset=utf-8" ，
            // type值是text，表示是文本这一大类；/后面的x-markdown是subtype，表示是文本这一大类下的markdown这一小类； charset=utf-8 则表示采用UTF-8编码
            = MediaType.parse("text/x-markdown; charset=utf-8");
    //表示image类里面的png格式
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        initOkHttpClient();
        bt_send = (Button) this.findViewById(R.id.bt_send);
        bt_sendfile = (Button) this.findViewById(R.id.bt_sendfile);
        bt_postsend = (Button) this.findViewById(R.id.bt_postsend);
        bt_downfile = (Button) this.findViewById(R.id.bt_downfile);
        bt_cancel = (Button) this.findViewById(R.id.bt_cancel);
        bt_send.setOnClickListener(this);
        bt_postsend.setOnClickListener(this);
        bt_sendfile.setOnClickListener(this);
        bt_downfile.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
//                getAsynHttp();
                getAsynForEngine();
                break;
            case R.id.bt_postsend:
                postAsynHttp();
                break;
            case R.id.bt_sendfile:
                postAsynFile();
                break;
            case R.id.bt_downfile:
//                downAsynFile();
              sendMultipart();
                break;
            case R.id.bt_cancel:
                cancel();
                break;
        }
    }


    private void initOkHttpClient() {
        // 目录/storage/emulated/0/Android/data/xl.gcs.com/cache
        File sdcache = getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        //创建请求客户端的Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //连接超时
                .connectTimeout(15, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(20, TimeUnit.SECONDS)
                //读取超时
                .readTimeout(20, TimeUnit.SECONDS)
                //设置缓存，参数是缓存地址和缓存容量
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        //建立请求客户端
        mOkHttpClient = builder.build();
    }

    /**
     * get异步请求
     */
    private void getAsynHttp() {
        //创建请求，传入url
        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        //设为get请求
        requestBuilder.method("GET", null);
        //建立请求
        Request request = requestBuilder.build();
        //发送请求类
        Call mcall = mOkHttpClient.newCall(request);
        //加入请求队列，适用于异步，如果是同步请求，就是execute()
        mcall.enqueue(new Callback() {
            //失败回调
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }

            //成功回调
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //转化成字符串
                String str = response.body().string();
                Log.i(TAG, str);
                //用主线程（ui线程）来执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getAsynForEngine(){
        OkHttpEngine.getInstance(OkHttpActivity.this).getAsynHttp("http://www.baidu.com", new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }
            @Override
            public void onResponse(String str) throws IOException{
                Log.d(TAG, str);
                Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * post异步请求
     */
    private void postAsynHttp() {
        //post请求的参数类
        RequestBody formBody = new FormBody.Builder()
                .add("ip", "59.108.54.37")
                .build();
        //请求类
        Request request = new Request.Builder()
                //url
                .url("http://ip.taobao.com/service/getIpInfo.php")
                //传入post参数
                .post(formBody)
                .build();
        //请求客户端
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Call call = mOkHttpClient.newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d(TAG, str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    /**
     * 异步上传文件
     */
    private void postAsynFile() {
        String filepath = "";
        //查看下sd卡的权限状态
        String state = Environment.getExternalStorageState();
        //如果是可读可写的，Environment.MEDIA_MOUNTED表示可读可写
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //获取sd卡根目录的路径
            filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //否则就获取隐藏目录的路径，需要root才能看到，不需要权限
            filepath = getFilesDir().getAbsolutePath();
        }
        File file = new File(filepath, "wangshu.txt");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                //这里post参数是文件类型，参数2是文件
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        //异步请求
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //返回的就是txt文件中的内容
                Log.d(TAG, response.body().string());
            }
        });
    }

    /**
     * 异步下载文件
     */
    private void downAsynFile() {
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        //请求类，传入url
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "文件下载失败", Toast.LENGTH_SHORT).show();
            }

            //响应成功的回调
            @Override
            public void onResponse(Call call, Response response) {
                //把结果转成字节输入流
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                String filepath = "";
                try {
                    //如果sd卡有可读写的权限
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        //就存入sd卡中
                        filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    } else {
                        //否则存入隐藏目录，需要root才能看见，不需要权限
                        filepath = getFilesDir().getAbsolutePath();
                    }
                    //创建文件
                    File file = new File(filepath, "wangshu.jpg");
                    if (null != file) {
                        //文件输出流
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[2048];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        //缓冲一下，有些没存完的会存掉
                        fileOutputStream.flush();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "文件存储成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //针对于创建文件失败的
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "文件存储失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException");
                    e.printStackTrace();
                }
            }
        });
    }

    //异步上传Multipart文件，这里只是举例，实际上传要应对服务器对应的类型
    private void sendMultipart() {
        RequestBody requestBody = new MultipartBody.Builder()
                // form 表单形式上传
                .setType(MultipartBody.FORM)
                //只是传参
                .addFormDataPart("title", "wangshu")
                //上传表单，参1是key值，参2是文件名，参3是文件（里面包括文件类型，文件路径）
                .addFormDataPart("image", "wangshu.jpg", RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }

    private void cancel() {
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                //CacheControl.FORCE_CACHE,仅仅使用缓存;CacheControl.FORCE_NETWORK,仅仅使用网络
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
        Call call = null;
        call = mOkHttpClient.newCall(request);
        final Call finalCall = call;
        //定时线程池，100毫秒后取消call
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                finalCall.cancel();
            }
        }, 100, TimeUnit.MILLISECONDS);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //缓存请求如果不等于null，就打印缓存请求的内容
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.d(TAG, "cache---" + str);
                } else {
                    //否则打印网络请求的内容
                    String str = response.networkResponse().toString();
                    Log.d(TAG, "network---" + str);
                }
            }
        });

    }
}
