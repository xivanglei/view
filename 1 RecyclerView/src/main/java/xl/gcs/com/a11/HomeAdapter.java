package xl.gcs.com.a11;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xianglei on 2018/4/9.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<Integer> mHeights;
    private List<String> mList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public HomeAdapter(List<String> list, Context context) {
        mList = list;
        mContext = context;
        setHeights();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    //删除单个数据，提供给外部使用
    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);        //针对移除一条数据的刷新,伴有动画
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        //下面几句为了让瀑布流的高度不一，同时比较网络布局的区别

        ViewGroup.LayoutParams lp = holder.tv.getLayoutParams();
        lp.height = mHeights.get(position);

        //这里设置多余了，上面的lp.height = mHeights.get(position);已经生效了
//        holder.tv.setLayoutParams(lp);
        holder.tv.setText(mList.get(position));

        if(mOnItemClickListener != null) {
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getLayoutPosition() 与getAdapterPosition() 区别是刷新adapter之后 有段时差，<16ms可能获取的数据不一样，具体需要再试
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.tv, position);
                }
            });

            holder.tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.tv, position);
                    return false;
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setHeights() {
        mHeights = new ArrayList<>();
        for(int i = 0; i < mList.size(); i++) {
            mHeights.add(100 + new Random().nextInt(300));
        }
    }
}
