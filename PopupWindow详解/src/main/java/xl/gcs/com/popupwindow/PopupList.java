package xl.gcs.com.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.Calendar;

/**
 * Created by xianglei on 2018/4/24.
 */

public class PopupList extends PopupWindow {

    private View conentView;
    private Activity context;
    private String[] mList;
    private OnItemClickListener listener;

    public PopupList(final Activity context, String[] list) {
        this(context, (AttributeSet) null);
        this.context = context;
        mList = list;
        initList();
    }

    //去边框主要是这个构造方法
    public PopupList(Activity context, AttributeSet attrs) {
        //这个是黑背景，就算自己设置了白的，也会有边框
        super(context);
        //这个是没背景，也就没边框
//        super(context, attrs, R.attr.popupWindowStyle);
    }

    private void initList() {
        LayoutInflater inflater = LayoutInflater.from(context);
        conentView = inflater.inflate(R.layout.popup_list, null);
        //获取手机屏幕的高度与宽度
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置显示的弹窗
        this.setContentView(conentView);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,mList);
        ListView listView = (ListView) conentView.findViewById(R.id.list);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listener != null) {
                    listener.onItemClick(mList[position]);
                }
            }
        });
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置点击外部就会消失
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
    }

    /**  第二种
     * 显示在控件的下中方
     *
     * @param parent parent
     */
    public void showAtDropDownCenter(View parent) {
        if (parent.getVisibility() == View.GONE) {
            this.showAtLocation(parent, 0, 0, 0);
        } else {
            // x y
            int[] location = new int[2];
            //获取控件(左上角位置)在整个屏幕内的绝对坐标
            parent.getLocationOnScreen(location);
            this.showAtLocation(parent, 0, location[0] + parent.getWidth() / 2 - this.getWidth() / 2, location[1] + parent.getHeight());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClick(String item);
    }
}
