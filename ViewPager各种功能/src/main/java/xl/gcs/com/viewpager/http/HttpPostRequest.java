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

import static com.android.volley.VolleyLog.TAG;


/**
 * Created by King on 2016/12/23.
 * 请求接口模型
 */

public class HttpPostRequest {
    private Context context = App.getInstance();

    ACache mCache = ACache.get(context);

    public void requestPost(final String url, Map<String, String> params, final OnRequestListener listener, final int id) {
        final String TAG = url + params.toString();
        if (!SystemUtil.isNetworkConnected()) {
            String asString = mCache.getAsString(TAG);
            if (!TextUtils.isEmpty(asString)) {
                ToastUtils.show("请检查您的网络状况");
                listener.onSuccess(url, asString, id);
                return;
            }
        }
        BaseVolleyRequest.StringRequestPost(url, TAG, params, new BaseStrVolleyInterFace(BaseStrVolleyInterFace.mListener, BaseStrVolleyInterFace.mErrorListener) {
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
            }
            @Override
            public void onError(VolleyError volleyError) {
                ToastUtils.show("请检查您的网络状况");
            }
        });
    }

    public void requestReturnResult(final String url, Map<String, String> params, final OnRequestListener listener, final int id) {

        BaseVolleyRequest.StringRequestPost(url, TAG, params, new BaseStrVolleyInterFace(BaseStrVolleyInterFace.mListener, BaseStrVolleyInterFace.mErrorListener) {

        @Override
        public void onSuccess(final String response) {
            listener.onSuccess(url, response, id);
        }
        @Override
        public void onError(VolleyError volleyError) {
            ToastUtils.show("请检查您的网络状况");
        }
    });
    }
}

