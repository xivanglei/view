package xl.gcs.com.viewpager.http;


import android.content.Context;
import android.text.TextUtils;


import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import xl.gcs.com.viewpager.app.App;
import xl.gcs.com.viewpager.utils.ACache;
import xl.gcs.com.viewpager.utils.JsonObjectUtils;
import xl.gcs.com.viewpager.utils.SystemUtil;
import xl.gcs.com.viewpager.utils.ToastUtils;



/**
 * Created by King on 2016/12/23.
 * 请求接口模型
 */

public class HttpPostRequest {
    private String mTAG = "";
    private boolean yetRequest = false;
    private Context context = App.getInstance();

    ACache mCache = ACache.get(context);

    public void requestPost(final String url, Map<String, String> params, final OnRequestListener listener, final int id) {
        final String TAG = url + params.toString();
        //如果刚刚也是请求这地址
        if(mTAG.equals(TAG)) {
            //如果还在请求中
            if(yetRequest) {
                //就返回，虽然封装的Volley请求里有判断TAG，先取消请求，但有些已经请求的就不会取消，导致请求好多条，结果返回过来，执行了好多次onSuccess
                return;
            }
        } else {
            //不是刚刚请求的地址，就记录这地址，准备下次比对
            mTAG = TAG;
        }
        //先设置已经发送请求，等结果出来再改成false
        yetRequest = true;
        //如果网络没连接
        if (!SystemUtil.isNetworkConnected()) {
            //读取缓存数据
            String asString = mCache.getAsString(TAG);
            //如果当时存了，并读取到了
            if (!TextUtils.isEmpty(asString)) {
                ToastUtils.show("请检查您的网络状况");
                //执行成功，因为只会成功才会存数据，所以当时只要有存都是成功状态
                listener.onSuccess(url, asString, id);
                //读取缓存也算执行成功，下次也能请求
                yetRequest = false;
                return;
            }
        }
        BaseVolleyRequest.StringRequestPost(url, TAG, params, new BaseVolleyRequest.ResponseCallback() {
            @Override
            public void onSuccess(final String response) {
                App.getInstance().mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int ret = JsonObjectUtils.getJsonInt(jsonObject, "ret");
                            if (ret==1001) {
                                listener.onSuccess(url, response, id);
                                mCache.put(TAG, response, 60 * 60 * 24 * 3);
                            } else {
                                listener.onFaild(url, response, id);
                                String message = JsonObjectUtils.getJsonString(jsonObject, "message");
                                ToastUtils.show(message);
                            }
                        } catch (JSONException e) {
                            ToastUtils.show("数据解析失败");
                            e.printStackTrace();
                        }
                    }
                });
                //结果已经执行完毕了，可以再次请求了
                yetRequest = false;
            }
            @Override
            public void onError(VolleyError error) {
                ToastUtils.show("请检查您的网络状况");
                //结果已经执行完毕了，可以再次请求了
                yetRequest = false;
            }
        });
    }

    public interface OnRequestListener {
        /**
         * 成功时回调
         */
        void onSuccess(String url, String result, int id);

        void onFaild(String url, String response, int id);
    }
}

