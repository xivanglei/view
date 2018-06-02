package xl.gcs.com.myapplication.butterknife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xl.gcs.com.myapplication.R;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class MoonFragment extends Fragment {
    @BindView(R.id.tv_toptext)
    TextView tv_toptext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moon, container,false);
        //在Fragment里使用，加上要返回的view就行了
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_toptext.setText("MoonFragment");
    }
}
