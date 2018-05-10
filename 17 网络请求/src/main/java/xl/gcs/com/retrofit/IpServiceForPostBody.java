package xl.gcs.com.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import xl.gcs.com.retrofit.model.Ip;
import xl.gcs.com.retrofit.model.IpModel;

/**
 * Created by Administrator on 2016/11/1 0001.
 */

public interface IpServiceForPostBody {
    //加入请求头，没有这行就经常会失败
    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent: MoonRetrofit"
    })
    //通过传入对象来传参数，用@Body注解参数对象，Retrofit会将Ip对象转换为字符串
    @POST("getIpInfo.php")
    Call<IpModel> getIpMsg(@Body Ip ip);
}
