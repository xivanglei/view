package xl.gcs.com.rxjava;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import xl.gcs.com.rxjava.model.HttpResult;
import xl.gcs.com.rxjava.model.IpData;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public interface IpServiceForPost{
    @FormUrlEncoded
    @POST("getIpInfo.php")
    Observable<HttpResult<IpData>> getIpMsg(@Field("ip") String first);
}
