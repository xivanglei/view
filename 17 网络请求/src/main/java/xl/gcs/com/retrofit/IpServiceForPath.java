package xl.gcs.com.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xl.gcs.com.retrofit.model.IpModel;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public interface IpServiceForPath {
    //加入请求头，没有这行就会经常失败
    @Headers({
            "Accept-Encoding: application/json",
            "User-Agent: MoonRetrofit"
    })
    //动态配置地址，下面通过@Path注解传入path，就会放入地址的{path}部分，下面"path"对应上面{path}
    @GET("{path}/getIpInfo.php?ip=59.108.54.37")
    Call<IpModel> getIpMsg(@Path("path") String path);
}
