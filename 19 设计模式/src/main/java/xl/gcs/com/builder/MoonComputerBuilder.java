package xl.gcs.com.builder;

/**
 * Created by xianglei on 2018/5/13.
 * 用于组装计算机，实现抽象的Builder类
 */

public class MoonComputerBuilder extends Builder {
    private Computer mComputer = new Computer();
    @Override
    public void buildCpu(String cpu) {
        mComputer.setCpu(cpu);
    }

    @Override
    public void buildMainboard(String mainboard) {
        mComputer.setMainBoard(mainboard);
    }

    @Override
    public void buildRam(String ram) {
        mComputer.setRam(ram);
    }

    @Override
    public Computer create() {
        return mComputer;
    }
}
