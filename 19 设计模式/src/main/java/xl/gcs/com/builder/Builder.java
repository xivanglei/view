package xl.gcs.com.builder;

/**
 * Created by xianglei on 2018/5/13.
 * 抽象Builder类，规范产品的组件，一半由子类实现
 */

public abstract class Builder {
    public abstract void buildCpu(String cpu);
    public abstract void buildMainboard(String mainboard);
    public abstract void buildRam(String ram);
    public abstract Computer create();
}
