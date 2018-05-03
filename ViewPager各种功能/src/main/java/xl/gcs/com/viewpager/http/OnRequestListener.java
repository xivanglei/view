package xl.gcs.com.viewpager.http;

/**
 * Created by King on 2016/12/21.
 * 请求回调
 */

public interface OnRequestListener {
    /**
     * 成功时回调
     */
    void onSuccess(String url, String result, int id);

    void onFaild(String url, String response, int id);
}
