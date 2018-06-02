package xl.gcs.com.myapplication.ipinfo;


import xl.gcs.com.myapplication.BaseView;
import xl.gcs.com.myapplication.model.IpInfo;

public interface IpInfoContract {
    interface Presenter {
        void getIpInfo(String ip);
    }

    interface View extends BaseView<Presenter> {
        void setIpInfo(IpInfo ipInfo);
        void showLoading();
        void hideLoading();
        void showError();
        boolean isActive();
    }
}
