package xl.gcs.com.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import xl.gcs.com.retrofit.model.IpModel;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public interface IpServiceForPost {
    //加入请求头，没有这行就会经常失败
    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent: MoonRetrofit"
    })
    //先用@FormUrlEncoded来表明这是一个表单请求
    @FormUrlEncoded
    //表示post请求
    @POST("getIpInfo.php")
    //@Field("ip")，表示传进来的参数，对应的是  键ip 值first
    Call<IpModel> getIpMsg(@Field("ip") String first);
}

