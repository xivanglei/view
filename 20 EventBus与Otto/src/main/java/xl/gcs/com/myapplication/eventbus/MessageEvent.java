package xl.gcs.com.myapplication.eventbus;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class MessageEvent<T> {

    private String message;
    private T t;

    public MessageEvent(String message, T t) {
        this.message = message;
        this.t = t;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
