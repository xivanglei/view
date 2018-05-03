package xl.gcs.com.viewpager.http;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import java.util.Map;

import xl.gcs.com.viewpager.app.App;


/**
 * Created by King on 2016/12/11.
 * Volley的封装
 */

public class BaseVolleyRequest {
    public static void StringRequestPost(String url,
                                         String tag, final Map<String, String> params,
                                         BaseStrVolleyInterFace vif) {
        App.getInstance().getHttpQueues().cancelAll(tag);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                vif.loadingListener(), vif.errorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringrequest.setTag(tag);
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        App.getInstance().getHttpQueues().add(stringrequest);
    }
}
