package xl.gcs.com.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import xl.gcs.com.retrofit.model.IpModel;

/**
 * Created by xianglei on 2018/5/9.
 */

public interface UploadFileForPartMap {
    //@Multipart表示允许多个@Part
    @Multipart
    //post请求
    @POST("user/photo")
    //这里第一个参数 @PartMap注解是可以上传多个文件，Map封装了上传的文件，参数2 是对应description键的值
    Call<IpModel> updateUser(@PartMap Map<String, RequestBody> photos, @Part("description") RequestBody description);
}
