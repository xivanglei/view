package xl.gcs.com.myapplication.dagger2.model;


import javax.inject.Inject;
import javax.inject.Named;

import xl.gcs.com.myapplication.dagger2.annotation.Gasoline;

/**
 * Created by Administrator on 2016/12/18 0018.
 */

public class Car {
    private Engine engine;

    //Car的有参构造需要参数Engine,但Engine是抽象类，不能直接实例化，可以在新建一个EngineModule类用@Module标注，返回子类
    @Inject
    //如果EngineModule类里返回两种子类，就需要区分，配合EngineModule的@Named或@Gasoline符，下面也用这两个符来区分
//    public Car(@Named("Diesel") Engine engine) {
    public Car(@Gasoline Engine engine) {
        this.engine = engine;
    }

    public String run() {
        return engine.work();
    }
}
