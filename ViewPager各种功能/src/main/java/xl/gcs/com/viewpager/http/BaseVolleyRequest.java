package xl.gcs.com.viewpager.http;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.Map;

import xl.gcs.com.viewpager.app.App;


/**
 * Created by King on 2016/12/11.
 * Volley的封装
 */

public class BaseVolleyRequest {

    public static void StringRequestPost(String url,
                                         String tag, final Map<String, String> params, final ResponseCallback callback) {
        App.getInstance().getHttpQueues().cancelAll(tag);
        StringRequest stringrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        callback.onSuccess(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringrequest.setTag(tag);
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        App.getInstance().getHttpQueues().add(stringrequest);
    }

    public interface ResponseCallback {
        void onSuccess(String result);
        void onError(VolleyError error);
    }
}
