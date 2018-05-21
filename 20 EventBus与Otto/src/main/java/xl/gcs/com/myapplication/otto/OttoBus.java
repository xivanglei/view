package xl.gcs.com.myapplication.otto;

import com.squareup.otto.Bus;

/**
 * Created by Administrator on 2016/11/17 0017.
 * 这里用双重检查的单例封装Bus类
 */

public class OttoBus extends Bus{
    private volatile static OttoBus bus;
    private OttoBus (){
    }
    public static OttoBus getInstance() {
        if (bus == null) {
            synchronized (OttoBus.class){
             if(bus==null){
                 bus = new OttoBus();
             }
            }
        }
        return bus;
    }
}
