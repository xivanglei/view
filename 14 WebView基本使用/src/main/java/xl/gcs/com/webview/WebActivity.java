package xl.gcs.com.webview;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by xianglei on 2018/4/24.
 */

public class WebActivity extends AppCompatActivity{

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        initView();
        initData(savedInstanceState);
    }


    protected void initData(Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        //加载网址
        mWebView.loadUrl(url);
        //加载html源码，源码可以通过post请求到，再传过来
//        mWebView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
    }

    protected void initView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //这里是动态加入WebView，加到Fragment里，看下布局里面没有WebView,因为直接放WebView即使在onDestroy上WebView.destroy()也没用，会造成内存泄漏，
        //所以先放个Fragment再动态添加WebView，OnDestroy的时候，先移除掉所有的子View(WebView),再WebView.destroy()，具体看下面OnDestroy
        mWebView = new WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        FrameLayout mFl_web_view = (FrameLayout) findViewById(R.id.fl_web_view);
        mFl_web_view.addView(mWebView);
        setDefaultWebSettings();
        //设置在WebView上继续打开，否则会在浏览器上打开的
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            // 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。这个函数我们可以做很多操作，比如我们读取到某些特殊的URL，于是就可以不打开地址，取消这个操作，进行预先定义的其他操作，这对一个程序是非常必要的。
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //如果是百度新闻就拦截
                if(url.equals("https://www.jimubox.com/")) {
                    Toast.makeText(WebActivity.this, "我不想你看积木盒子", Toast.LENGTH_SHORT).show();
                    //退回
                    mWebView.goBack();
                    return true;
                }
                //否则不拦截
                return false;
            }
        });
    }

    //返回键，如果 WebView能返回，就让WebView返回，而不是关闭页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public  void setDefaultWebSettings() {
        WebSettings webSettings = mWebView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置自适应屏幕，两者合用
        //设置网页布局类型：1、LayoutAlgorithm.NARROW_COLUMNS ： 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //是否允许在WebView中访问内容URL,默认是true
        webSettings.setAllowContentAccess(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);

        webSettings.setSaveFormData(true);// 保存表单数据
        webSettings.setGeolocationEnabled(true);// 启用地理定位
        webSettings.setSupportMultipleWindows(true);// 设置WebView是否支持多屏窗口，默认false，不支持。
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(this.getDir("appcache", 0).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);

        if (isNetworkAvailable()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            //加载网页的一种方式，,可以加载html数据，只要把第二个参数位置放入html源码就行了，这里为了加空数据
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清空所有记录，如果页面加载未完成，就会失效，所以上面先加一个空的数据，这里就不会失效
            mWebView.clearHistory();
            //把WebView从Fragment里移除,因为是动态加载的，所以可以这样移除
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            //销毁WebView
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
