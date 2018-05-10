package xl.gcs.com.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import xl.gcs.com.retrofit.model.IpModel;

/**
 * Created by xianglei on 2018/5/9.
 */

public interface UploadFileForPart {
    //@Multipart表示允许多个@Part
    @Multipart
    //post请求
    @POST("user/photo")
    //第一个参数 MultipartBody.Part是准备上传的图片文件，参数2 是对应description键的值
    Call<IpModel> updateUser(@Part MultipartBody.Part photo, @Part("description")RequestBody description);
}
