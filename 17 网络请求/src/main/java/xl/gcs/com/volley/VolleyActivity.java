package xl.gcs.com.volley;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xl.gcs.com.R;


public class VolleyActivity extends AppCompatActivity {
    private static final String TAG="Volley";
    private ImageView iv_image;
    private Button bt_send;
    private NetworkImageView nv_image;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        iv_image = (ImageView) this.findViewById(R.id.iv_image);
        bt_send = (Button) this.findViewById(R.id.bt_send);
        nv_image = (NetworkImageView) this.findViewById(R.id.nv_image);
        //创建请求队列，如果请求不是特别频繁，可以只有一个队列，getApplicationContext()获取全局队列，如果请求很多，可以一个Activity一个队列
        mQueue = Volley.newRequestQueue(getApplicationContext());
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用StringRequest发送请求，返回的是String数据
//                UseStringRequest();
//                  UseJsonRequest();
//                UseImageRequest();
//                UseImageLoader();
                UseNetworkImageView();
            }
        });
    }


    private void UseStringRequest() {
        //创建请求队列，1是请求方式，2是地址，3是成功回调，4是失败回调  使用StringRequest发送请求，返回的是String数据
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, "https://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });
        //将请求添加在请求队列中
        mQueue.add(mStringRequest);
    }

    private void UseJsonRequest() {
        String requestBody = "ip=59.108.54.37";
        //使用JsonObjectRequest,直接返回Json数据，其实用法差不多，1234参数一样，这里url直接把参数写进去了，不过一般是通过下面这种方式传参
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://ip.taobao.com/service/getIpInfo.php?ip=59.108.54.37",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        IpModel ipModel = new Gson().fromJson(response.toString(), IpModel.class);
                        if (null != ipModel && null != ipModel.getData()) {
                            String city = ipModel.getData().getCity();
                            Log.d(TAG, city);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        })
            //这里通过复写方法再次添加参数，跟上面重复了，只是介绍下一般是这种添加参数的方式
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ip", "59.108.54.37");
                return params;
            }
        };
        //把请求添加到请求队列中
        mQueue.add(mJsonObjectRequest);
    }

    private void UseImageRequest() {
        //使用ImageRequest加载图片，这方法已经过时了，1,url 2,成功回调，3，失败回调，4，宽度，5高度，6图片质量，7失败回调
        ImageRequest imageRequest = new ImageRequest(
                "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        //成功后会返回位图，把位图设置进iv_image
                        iv_image.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //失败的话设置默认的图片
                iv_image.setImageResource(R.drawable.ico_default);
            }
        });
        //把请求添加到队列中
        mQueue.add(imageRequest);
    }


    private void UseImageLoader() {
        //ImageLoader内部也是通过ImageRequest实现，有缓存功能，还会过滤重复连接，避免重复请求，先加载默认图，成功后再换图。1是请求队列，2是自定义缓存类
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        //1是要显示的图片，2是预显示的默认图，3是失败的图
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv_image, R.drawable.ico_default, R.drawable.ico_default);
        //get传入地址和刚刚的listener就好了
//        imageLoader.get("http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg", listener);
        //还可以限制宽高，这里如果0就相当于不限制，如果限制50，而非要放在150的框里就会显示的很模糊，最好限制的宽高跟显示的宽高匹配
        imageLoader.get("http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg", listener, 150, 150);
    }

    //这里加载volley的自定义ImageView控件，里面封装了一些方法，可以不用管宽高，直接在控件里设置就好，如果不限制就wrap_content
    private void UseNetworkImageView() {
        //第一步跟上面一样
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        //加载默认图
        nv_image.setDefaultImageResId(R.drawable.ico_default);
        //显示出错图
        nv_image.setErrorImageResId(R.drawable.ico_default);
        //设置地址和ImageLoader就好了
        nv_image.setImageUrl("http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg",
                imageLoader);

    }
}
