package xl.gcs.com.builder;

/**
 * Created by xianglei on 2018/5/13.
 * 导演类，用来规范组装计算机的流程规范，先安装主板，再安装CPU，最后安装内存，并组装成计算机
 */

public class Director {
    Builder mBuilder = null;
    public Director(Builder builder) {
        mBuilder = builder;
    }
    public Computer createComputer(String cpu, String mainboard, String ram) {
        mBuilder.buildMainboard(mainboard);
        mBuilder.buildCpu(cpu);
        mBuilder.buildRam(ram);
        return mBuilder.create();
    }
}
