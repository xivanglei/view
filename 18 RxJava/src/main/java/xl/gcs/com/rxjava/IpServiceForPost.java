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
    //先用@FormUrlEncoded来表明这是一个表单请求
    @FormUrlEncoded
    //表示post请求
    @POST("getIpInfo.php")
    //@Field("ip")，表示传进来的参数，对应的是  键ip 值first，本来返回的应该是call，由于要结合RxJava使用，就返回Observable了
    //这里的数据类型HttpResult<IpData>已经被封装过了，蛮好的，可以学习下
    Observable<HttpResult<IpData>> getIpMsg(@Field("ip") String first);
}
