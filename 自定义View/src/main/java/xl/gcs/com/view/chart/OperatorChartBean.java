package xl.gcs.com.view.chart;

/**
 * Created by xianglei on 2018/5/15.
 */

public class OperatorChartBean {
    private int year;
    private int month;
    private float telephone_charge;
    private int telephone_count;

    public OperatorChartBean(int year, int month, float telephone_charge, int telephone_count) {
        this.year = year;
        this.month = month;
        this.telephone_charge = telephone_charge;
        this.telephone_count = telephone_count;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public float getTelephone_charge() {
        return telephone_charge;
    }

    public int getTelephone_count() {
        return telephone_count;
    }
}
