package xl.gcs.com.a11;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * 自带刷新的 recyclerView 除了导入recyclerView 还需要
 * implementation 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.3' 刷新
 *
 * 先在 build.gradle(Project:XXXX) 的 repositories 添加:
 allprojects {
 repositories {
 ...
 maven { url "https://jitpack.io" }
 }
 }
 implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.30'  BRVAH  RecyclerAdapter框架
 * Created by xianglei on 2018/4/13.
 */

public class SuperRecyclerView extends FrameLayout {

    private RecyclerView mRecycler;
    private SmartRefreshLayout mSmartRefreshLayout;

    public SuperRecyclerView(Context context) {
        this(context, null);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.view_recycler, null);
        addView(view);
        mRecycler = (RecyclerView) view.findViewById(R.id.my_super_recycler);
        mSmartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smartRefreshLayout);
    }

    /**
     * 设置layoutManager
     *
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecycler.setLayoutManager(layoutManager);
    }

    /**
     * @param adapter
     */
    public void setAdapter(BaseQuickAdapter adapter) {
        mRecycler.setAdapter(adapter);
    }

    /**
     * 刷新监听
     *
     * @param listener
     */
    public void setOnRefreshListerner(OnRefreshListener listener) {
        mSmartRefreshLayout.setOnRefreshListener(listener);
    }

    /**
     * 加载更多监听
     *
     * @param listener
     */
    public void setOnLoadMoreListerner(OnLoadmoreListener listener) {
        if (listener != null) {
            mSmartRefreshLayout.setOnLoadmoreListener(listener);
        }
    }

    /**
     * 结束刷新
     */
    public void finishLoading() {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadmore();
    }


    /**
     * 设置能否加载更多
     *
     * @param enable
     */
    public void loadMoreEnable(boolean enable) {
        mSmartRefreshLayout.setEnableLoadmore(enable);
    }

    public void refreshEnable(boolean enable) {
        mSmartRefreshLayout.setEnableRefresh(enable);
    }

    public void setRefreshing() {
        mSmartRefreshLayout.autoRefresh(100);
    }

    public RecyclerView getRecyclerView() {
        return mRecycler;
    }

    public SmartRefreshLayout getRefreshView() {
        return mSmartRefreshLayout;
    }
}

