package xl.gcs.com.rxjava.bus;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import rx.Subscription;
import rx.functions.Action1;
import xl.gcs.com.rxjava.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RxBusFragment extends Fragment {

    private TextView tv_text;
    private Subscription subscription;
    public RxBusFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rx_bus2, container,false);
        tv_text= (TextView) view.findViewById(R.id.tv_text);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //刚刚已经通过RxBus.subject发送数据了，这里再过滤下，相当于不管之前有多少类发送了数据，这里只接受MessageEvent类型的消息
        //指定新的Action1(观察者)来处理数据
        subscription= RxBus.getInstance().toObservable(MessageEvent.class).subscribe(new Action1<MessageEvent>() {
            @Override
            public void call(MessageEvent messageEvent) {
                if(messageEvent!=null) {
                    tv_text.setText(messageEvent.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //如果销毁了还没完成或取消，就取消掉
        if(subscription!=null&&!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
