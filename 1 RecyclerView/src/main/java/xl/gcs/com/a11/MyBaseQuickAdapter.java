package xl.gcs.com.a11;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by xianglei on 2018/4/13.
 */

public class MyBaseQuickAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    private OnItemClickListener mOnItemClickListener;

    public MyBaseQuickAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, Integer item) {
        final TextView textView =  helper.getView(R.id.tv_item);
        textView.setText(item + "");
        if(mOnItemClickListener != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getLayoutPosition() 与getAdapterPosition() 区别是刷新adapter之后 有段时差，<16ms可能获取的数据不一样，具体需要再试
                    int position = helper.getLayoutPosition();
                    mOnItemClickListener.onItemClick(textView, position);
                }
            });

            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = helper.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(textView, position);
                    return false;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    public void setOnMyItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
