package xl.gcs.com.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import xl.gcs.com.retrofit.model.IpModel;

/**
 * Created by Administrator on 2016/11/1 0001.
 */

public interface IpServiceForQueryMap {
    @GET("getIpInfo.php")
    Call<IpModel> getIpMsg(@QueryMap Map<String, String> options);
}
