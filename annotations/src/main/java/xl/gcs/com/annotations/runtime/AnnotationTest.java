package xl.gcs.com.annotations.runtime;

/**
 * Created by Administrator on 2016/12/11 0011.
 */

public class AnnotationTest {
    //使用注解，如果没有default指定默认值的话，要为成员变量指定值
    @GET(value = "http://ip.taobao.com/59.108.54.37")
    public String getIpMsg() {
     return "";
    }
    @GET(value = "http://ip.taobao.com/")
    public String getIp() {
        return "";
    }
}
