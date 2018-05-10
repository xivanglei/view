package xl.gcs.com.viewpager.base;


import android.text.TextUtils;


import com.google.gson.Gson;

import java.util.Map;

import xl.gcs.com.viewpager.http.HttpPostRequest;
import xl.gcs.com.viewpager.utils.ToastUtils;


/**
 * Created by ASUS on 2017/8/26.
 */
public abstract class BaseHttpActivity extends BaseActivity implements HttpPostRequest.OnRequestListener {
    protected HttpPostRequest mPostRequest = new HttpPostRequest();

    protected final void postHttp(String url, Map<String, String> params) {
        if (TextUtils.isEmpty(url)) {
            ToastUtils.show("访问地址出错");
            return;
        }
        mPostRequest.requestPost(url, params, this, 0);
    }
    protected <T> T parseJsonToBean(String json, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

}
