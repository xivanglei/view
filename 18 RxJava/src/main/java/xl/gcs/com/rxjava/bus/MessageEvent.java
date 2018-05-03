package xl.gcs.com.rxjava.bus;

/**
 * Created by Administrator on 2016/12/3 0003.
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
