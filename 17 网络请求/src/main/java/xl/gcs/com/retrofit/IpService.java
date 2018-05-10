package xl.gcs.com.retrofit;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import xl.gcs.com.retrofit.model.IpModel;

public interface IpService {
    //加入请求头，没有这行就经常会失败，这里有两个文件头，用{}包起来，中间用都好隔开，如果只有一个文件头的话，就不用大括号，也可以用动态方式添加，下面介绍
    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent: MoonRetrofit"
    })
    //表示get请求括号里是地址
    @GET("getIpInfo.php?ip=59.108.54.37")
    //定义了getIpMsg()方法，返回Call<IpModel>
    Call<IpModel> getIpMsg();

    //动态方式添加消息报头
    Call<IpModel> getIpMsg(@Header("Location") String location);
}
