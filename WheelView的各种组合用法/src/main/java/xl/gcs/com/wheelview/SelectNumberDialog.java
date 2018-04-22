package xl.gcs.com.wheelview;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.contrarywind.view.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianglei on 2018/4/17.
 */

public class SelectNumberDialog extends Dialog {

    @BindView(R.id.wheel_view1)
    WheelView mWheelView1;

    private OnCommitClickListener listener;
    private Context mContext;
    private List<String> options = new ArrayList<>();
    private int mMin, mMax;

    public SelectNumberDialog(@NonNull Context context, int min, int max) {
        super(context, R.style.dialog);
        mContext = context;
        mMin = min;
        mMax = max;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_wheelview_option1, null);
        setContentView(view);
        ButterKnife.bind(this);
        //是否循环
        mWheelView1.setCyclic(false);
        //居中
        mWheelView1.setGravity(Gravity.CENTER);
        //初始化数据
        initData();
        //设置数据
        mWheelView1.setAdapter(new ArrayWheelAdapter(options));
    }

    private void initData() {
        for(int i = mMin; i <= mMax; i++) {
            options.add(i + "");
        }
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if (listener!=null) {
            listener.onCommitClick(options.get(mWheelView1.getCurrentItem()));
        }
        dismiss();
    }

    public void setOnCommitClickListener(OnCommitClickListener onCommitClickListener){
        this.listener = onCommitClickListener;
    }
    public interface OnCommitClickListener {
        void onCommitClick(String number);
    }
}
