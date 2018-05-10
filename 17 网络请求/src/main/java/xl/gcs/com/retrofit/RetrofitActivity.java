package xl.gcs.com.retrofit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.lang.*;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xl.gcs.com.R;
import xl.gcs.com.retrofit.model.Ip;
import xl.gcs.com.retrofit.model.IpModel;

public class RetrofitActivity extends AppCompatActivity {
    private Button bt_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        bt_request = (Button) findViewById(R.id.bt_request);
        bt_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getIpInformation();
//                getIpInformationForPath("service");
//                getIpInformationForQuery("59.108.54.37");
//                postIpInformation("59.108.54.37");
//                postIpInformationForBody("59.108.54.37");
            }
        });
    }

    /**
     * 普通GET请求
     */
    private void getIpInformation() {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                //传入基本地址，一会还要拼接上get里面的地址字段
                .baseUrl(url)
                //增加返回值为Gson的支持
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //获取到之前定义的get接口
        IpService ipService = retrofit.create(IpService.class);
        //得到Call对象
        Call<IpModel> call = ipService.getIpMsg();
        //加入请求队列，回调Callback是运行在UI线程的，就不用handler了，如果想同步请求就call.execute(),中断网络请求就call.cancel()
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                //回调回来的response已经通过Gson解析了，可以直接用
                String country = response.body().getData().getCountry();
                Log.i("wangshu", "country" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

    /**
     * @param path
     * @Path方式GET请求
     * 可以传入参数，代替地址中某个注解的字段
     */
    private void getIpInformationForPath(String path) {
        String url = "http://ip.taobao.com/";
        Retrofit retrofit = new Retrofit.Builder()
                //基本地址
                .baseUrl(url)
                //加入Gson支持
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //获取自定义的接口
        IpServiceForPath ipService = retrofit.create(IpServiceForPath.class);
        //取得Call对象，IpServiceForPath的getIpMsg里注解了Path,把path放进去就会代入上面注解的get里面的{Path}
        Call<IpModel> call = ipService.getIpMsg(path);
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().getCountry();
                Log.i("wangshu", "country" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    /**
     * @param ip
     * @Query方式GET请求
     * 动态配置地址，下面通过@Path注解传入path，就会放入地址的{path}部分，下面"path"对应上面{path}
     */
    private void getIpInformationForQuery(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                //传入基本地址，一会还要拼接上get里面的地址字段
                .baseUrl(url)
                //增加返回值为Json的支持
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForQuery ipService = retrofit.create(IpServiceForQuery.class);
        //这里传入ip，getIpMsg中注解了@Query,会把ip放进去
        Call<IpModel> call = ipService.getIpMsg(ip);
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().getCountry();
                Log.i("wangshu", "country" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 传输数据类型为键值对的POST请求
     *
     * @param ip
     * 传入ip参数，发送post请求,键ip是在方法里注解的
     */
    private void postIpInformation(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForPost ipService = retrofit.create(IpServiceForPost.class);
        Call<IpModel> call = ipService.getIpMsg(ip);
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().getCountry();
                Log.i("wangshu", "country" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * 传输数据类型Json字符串的POST请求
     *
     * @param ip
     * 传入对象发送post请求，方法里通过@Body注解
     */
    private void postIpInformationForBody(String ip) {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IpServiceForPostBody ipService = retrofit.create(IpServiceForPostBody.class);
        Call<IpModel> call = ipService.getIpMsg(new Ip(ip));
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().getCountry();
                Log.i("wangshu", "country" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

    //上传单个文件，这里的url,IpModel,wangshu.png等等都是乱写的，主要看格式，如果要上传多个文件就要通过Map放入多个文件，然后在方法里通过@PartMap注解，具体看UploadFileForPartMap接口
    public void uploadFileForPart() {
        String url = "http://ip.taobao.com/service/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        File file = new File(Environment.getExternalStorageDirectory(), "wangshu.png");
        //创建请求主体，参数1是文件类型，2是文件
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photos", "wangshu.png", photoRequestBody);
        UploadFileForPart uploadFile = retrofit.create(UploadFileForPart.class);
        Call<IpModel> call = uploadFile.updateUser(photo, RequestBody.create(null, "wangshu"));
        call.enqueue(new Callback<IpModel>() {
            @Override
            public void onResponse(Call<IpModel> call, Response<IpModel> response) {
                String country = response.body().getData().getCountry();
                Log.i("wangshu", "country" + country);
                Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<IpModel> call, Throwable t) {

            }
        });
    }

}
