package xl.gcs.com.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import xl.gcs.com.retrofit.model.IpModel;

/**
 * Created by Administrator on 2016/9/9 0009.
 */
public interface IpServiceForQuery{
    //加入请求头，没有这行就会经常失败
    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent: MoonRetrofit"
    })
    @GET("getIpInfo.php")
    //这里动态指定查询条件，可以不在地址中指定ip，动态传入参数，根据参数访问哪个ip
    Call<IpModel> getIpMsg(@Query("ip") String ip);
}
