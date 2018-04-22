package xl.gcs.com.wheelview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xianglei on 2018/4/16.
 */

public class SelectCityDialog extends Dialog {

    @BindView(R.id.wheel_view1)
    WheelView mWheelView1;
    @BindView(R.id.wheel_view2)
    WheelView mWheelView2;
    @BindView(R.id.wheel_view3)
    WheelView mWheelView3;

    private List<ProvinceData> mData;
    private List<String> options = new ArrayList<>();
    private List<String> options2 = new ArrayList<>();
    private List<String> options3 = new ArrayList<>();
    private int provinceNumber = 0;

    private OnCommitClickListener listener;
    private Context mContext;

    public SelectCityDialog(@NonNull Context context) {
        super(context, R.style.dialog2);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_wheelview_option3, null);
        setContentView(view);
        ButterKnife.bind(this);
        initData();
        //点击对话框外面就取消
        setCanceledOnTouchOutside(true);
        mWheelView1.setAdapter(new ArrayWheelAdapter(options));
        mWheelView1.setGravity(Gravity.CENTER);
        mWheelView1.setCurrentItem(0);
        mWheelView1.setCyclic(false);
        mWheelView1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                //当省数据改变的时候，先清空市数据
                options2.clear();
                //再根据现在的数据位置，调取那个省位置的所有市数据
                for(ProvinceData.CityData i : mData.get(index).city) {
                    options2.add(i.cityName);
                    //存一下这个省的位置，等会市数据变化的时候才能知道是哪个省
                    provinceNumber = index;
                }
                //wheelView没有刷新，就选一次首位置当刷新数据吧
                mWheelView2.setCurrentItem(0);
                //省变化，也要清空县数据
                options3.clear();
                //根据这个省，第一个市的位置更新县
                for(String i : mData.get(provinceNumber).city[0].area) {
                    options3.add(i);
                }
                mWheelView3.setCurrentItem(0);
            }
        });
        mWheelView2.setCyclic(false);
        mWheelView2.setAdapter(new ArrayWheelAdapter(options2));
        mWheelView2.setGravity(Gravity.CENTER);
        mWheelView2.setCurrentItem(0);
        mWheelView2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                //市变化，县清空
                options3.clear();
                //再调出刚刚存起来的省，调出所有市，再根据现在的市位置调出所有县，加入县数据
                for(String i : mData.get(provinceNumber).city[index].area) {
                    options3.add(i);
                }
                mWheelView3.setCurrentItem(0);
            }
        });
        mWheelView3.setCyclic(false);
        mWheelView3.setAdapter(new ArrayWheelAdapter(options3));
        mWheelView3.setGravity(Gravity.CENTER);
        mWheelView3.setCurrentItem(0);
    }

    private void initData() {
        //从assets的province.json中读取数据
        InputStream is = mContext.getClass().getClassLoader().getResourceAsStream("assets/" + "province.json");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //通过Gson 解析数据
        mData = handleWeatherResponse(stringBuilder.toString());
        for(ProvinceData i : mData) {
            //加入省数据
            options.add(i.provinceName);
        }
        //加入第一个省的市数据
        for(ProvinceData.CityData i : mData.get(0).city) {
            options2.add(i.cityName);
        }
        //加入第一个省的第一个市的第一个县数据，因为是初始化，这样就够了
        for(String i : mData.get(0).city[0].area) {
            options3.add(i);
        }
    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        if (listener!=null) {
            listener.onCommitClick(options.get(mWheelView1.getCurrentItem()), options2.get(mWheelView2.getCurrentItem()),
                    options3.get(mWheelView3.getCurrentItem()));
        }
        dismiss();
    }

    public void setOnCommitClickListener(OnCommitClickListener onCommitClickListener){
        this.listener = onCommitClickListener;
    }
    public interface OnCommitClickListener {
        void onCommitClick(String province, String city, String county);
    }

    public List<ProvinceData> handleWeatherResponse(String response) {
        try {
            Gson gson = new Gson();
            List<ProvinceData> provinceData = gson.fromJson(response, new TypeToken<List<ProvinceData>>(){}.getType());

            return provinceData;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
