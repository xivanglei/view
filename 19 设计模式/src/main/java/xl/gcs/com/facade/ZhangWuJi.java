package xl.gcs.com.facade;

/**
 * Created by xianglei on 2018/5/13.
 * 外观类
 */

public class ZhangWuJi {
    private JingMai mJingMai;
    private ZhaoShi mZhaoShi;
    private NeiGong mNeiGong;

    public ZhangWuJi() {
        mJingMai = new JingMai();
        mZhaoShi = new ZhaoShi();
        mNeiGong = new NeiGong();
    }

    public void QianKun() {
        mJingMai.jingmai();
        mNeiGong.QianKun();
    }

    public void QiShang() {
        mJingMai.jingmai();
        mNeiGong.JiuYang();
        mZhaoShi.QiShangQuan();
    }
}
