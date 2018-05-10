package xl.gcs.com.httpclientandhttpurlconnection;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xl.gcs.com.R;

//需要再build的 android闭包里添加 useLibrary 'org.apache.http.legacy'
public class HttpClientAndHttpURLConnectionActivity extends AppCompatActivity {

    private static final String TAG="HttpUrl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httpclient_and_httpurlconnection);
        ButterKnife.bind(this);
    }

    /**
     * HttpClient GET请求网络
     */
    private void useHttpClientGetThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                useHttpClientGet("http://www.baidu.com");
            }
        }).start();
    }
    /**
     * HttpClient POST请求网络
     */
    private void useHttpClientPostThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                useHttpClientPost("http://ip.taobao.com/service/getIpInfo.php");
            }
        }).start();
    }

    /**
     * HttpUrlConnection POST请求网络
     */
    private void useHttpUrlConnectionPostThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                useHttpUrlConnectionPost("http://ip.taobao.com/service/getIpInfo.php");
            }
        }).start();
    }


    /**
     * 这个方法是设置请求参数，并把参数加到 请求客户端mHttpClient里，然后返回
     */
    private HttpClient createHttpClient() {
        //创建参数
        HttpParams mDefaultHttpParams = new BasicHttpParams();
        //设置连接超时
        HttpConnectionParams.setConnectionTimeout(mDefaultHttpParams, 15000);
        //设置请求超时
        HttpConnectionParams.setSoTimeout(mDefaultHttpParams, 15000);
        //设置是否延迟发送
        HttpConnectionParams.setTcpNoDelay(mDefaultHttpParams, true);
        //HTTP 协议的版本,有1.1/1.0/0.9
        HttpProtocolParams.setVersion(mDefaultHttpParams, HttpVersion.HTTP_1_1);
        //字符集
        HttpProtocolParams.setContentCharset(mDefaultHttpParams, HTTP.UTF_8);
        // 设置异常处理机制，这里是持续握手
        HttpProtocolParams.setUseExpectContinue(mDefaultHttpParams, true);
        //创建请求客户端HttpClient加入参数
        HttpClient mHttpClient = new DefaultHttpClient(mDefaultHttpParams);
        return mHttpClient;

    }

    /**
     * 使用HttpClient的get请求网络
     *
     * @param url
     */

    private void useHttpClientGet(String url) {
        //创建get请求类，加入url
        HttpGet mHttpGet = new HttpGet(url);
        //添加请求头，Connection：客户机的连接状态(close：关闭连接  Keep-Alive：不会断开连接)
        mHttpGet.addHeader("Connection", "Keep-Alive");
        try {
            //创建客户端
            HttpClient mHttpClient = createHttpClient();
            //通过客户端HttpClient执行get请求HttpGet，返回响应数据类HttpResponse
            HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
            //HttpEntity其实相当于一个消息实体，内容是http传送的报文（这里可以说是html，css等等文件）。
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            //获得请求状态码，如果等于200就是请求成功了
            int code = mHttpResponse.getStatusLine().getStatusCode();
            if (null != mHttpEntity) {
                //通过mHttpEntity.getContent()返回输入流
                InputStream mInputStream = mHttpEntity.getContent();
                //把输入流解读成字符创
                String respose = converStreamToString(mInputStream);
                Log.d(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
                //输入流用完要关闭
                mInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useHttpClientPost(String url) {
        //创建post请求类
        HttpPost mHttpPost = new HttpPost(url);
        //添加请求头，Connection：客户机的连接状态(close：关闭连接  Keep-Alive：不会断开连接)
        mHttpPost.addHeader("Connection", "Keep-Alive");
        try {
            //创建客户端
            HttpClient mHttpClient = createHttpClient();
            //List参数
            List<NameValuePair> postParams = new ArrayList<>();
            //添加要传递的参数
            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));
            //把参数加进去
            mHttpPost.setEntity(new UrlEncodedFormEntity(postParams));
            //执行post请求
            HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
            //HttpEntity其实相当于一个消息实体，内容是http传送的报文（这里可以说是html，css等等文件）。
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            //获得请求状态码
            int code = mHttpResponse.getStatusLine().getStatusCode();
            if (null != mHttpEntity) {
                //通过mHttpEntity.getContent()返回输入流
                InputStream mInputStream = mHttpEntity.getContent();
                //把输入流解读成字符创
                String respose = converStreamToString(mInputStream);
                Log.d(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
                //输入流需要关闭
                mInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将请求结果装潢为String类型
     *
     * @param is InputStream
     * @return String
     * @throws IOException
     */
    private String converStreamToString(InputStream is) throws IOException {
        //字符缓冲流
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        //读取一行，如果不等于null说明还有数据
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String respose = sb.toString();
        return respose;
    }

    private void useHttpUrlConnectionPost(String url) {
        InputStream mInputStream = null;
        //先配置参数
        HttpURLConnection mHttpURLConnection = UrlConnManager.getHttpURLConnection(url);
        try {
            //声明实例post参数
            List<NameValuePair> postParams = new ArrayList<>();
            //要传递的参数
            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));
            //这方法把参数数据postParams里按照格式写成字符串，写入输出流mHttpURLConnection.getOutputStream()
            UrlConnManager.postParams(mHttpURLConnection.getOutputStream(), postParams);
            //连接，上面已经加入参数了
            mHttpURLConnection.connect();
            //获取输入流
            mInputStream = mHttpURLConnection.getInputStream();
            //获取请求码
            int code = mHttpURLConnection.getResponseCode();
            //把请求到的内容转成字符串
            String respose = converStreamToString(mInputStream);
            Log.d(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.bt_httpclient_get, R.id.bt_httpclient_post, R.id.bt_httpurlconnection_post})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_httpclient_get:
                useHttpClientGetThread();
                break;
            case R.id.bt_httpclient_post:
                useHttpClientPostThread();
                break;
            case R.id.bt_httpurlconnection_post:
                useHttpUrlConnectionPostThread();
                break;
            default:
                break;
        }
    }
}
