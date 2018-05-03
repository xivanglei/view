package xl.gcs.com.viewpager.utils;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by admin on 2018/3/14.
 */

public class JsonObjectUtils {
    public static String getJsonString(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch (Exception e) {
            return "";
        }
    }

    public static int getJsonInt(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getInt(name);
        } catch (Exception e) {
            return -1;
        }
    }

    public static  <T> T parseJsonToBean(String json, Class<T> cls) {
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
