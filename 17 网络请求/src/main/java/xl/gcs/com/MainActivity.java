package xl.gcs.com;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;


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

//需要再build的 android闭包里添加 useLibrary 'org.apache.http.legacy'
public class MainActivity extends Activity {
private static final String TAG="HttpUrl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        useHttpClientGetThread();
//      useHttpClientPostThread();
//      useHttpUrlConnectionPostThread();
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
        //设置参数，方便以后复用http链接
        mHttpGet.addHeader("Connection", "Keep-Alive");
        try {
            //创建客户端
            HttpClient mHttpClient = createHttpClient();
            HttpResponse mHttpResponse = mHttpClient.execute(mHttpGet);
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            int code = mHttpResponse.getStatusLine().getStatusCode();
            if (null != mHttpEntity) {
                InputStream mInputStream = mHttpEntity.getContent();
                String respose = converStreamToString(mInputStream);
                Log.d(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
                mInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void useHttpClientPost(String url) {
        HttpPost mHttpPost = new HttpPost(url);
        mHttpPost.addHeader("Connection", "Keep-Alive");
        try {
            HttpClient mHttpClient = createHttpClient();
            List<NameValuePair> postParams = new ArrayList<>();
            //要传递的参数
            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));
            mHttpPost.setEntity(new UrlEncodedFormEntity(postParams));
            HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
            HttpEntity mHttpEntity = mHttpResponse.getEntity();
            int code = mHttpResponse.getStatusLine().getStatusCode();
            if (null != mHttpEntity) {
                InputStream mInputStream = mHttpEntity.getContent();
                String respose = converStreamToString(mInputStream);
                Log.d(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String respose = sb.toString();
        return respose;
    }

    private void useHttpUrlConnectionPost(String url) {
        InputStream mInputStream = null;
        HttpURLConnection mHttpURLConnection = UrlConnManager.getHttpURLConnection(url);
        try {
            List<NameValuePair> postParams = new ArrayList<>();
            //要传递的参数
            postParams.add(new BasicNameValuePair("ip", "59.108.54.37"));
            UrlConnManager.postParams(mHttpURLConnection.getOutputStream(), postParams);
            mHttpURLConnection.connect();
            mInputStream = mHttpURLConnection.getInputStream();
            int code = mHttpURLConnection.getResponseCode();
            String respose = converStreamToString(mInputStream);
            Log.d(TAG, "请求状态码:" + code + "\n请求结果:\n" + respose);
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
