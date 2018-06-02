package xl.gcs.com.rxjava.model;

//这里对数据类型进行了封装，由于返回的数据固定有int类型的code,所以，每次只要更改data类型就行了，比如HttpResult<IpData>
//getData就会返回IpData的数据
public class HttpResult<T> {
    private int code;
    private T data;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
