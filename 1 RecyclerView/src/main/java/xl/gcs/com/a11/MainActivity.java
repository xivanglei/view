package xl.gcs.com.a11;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private SuperRecyclerView mSuperRecyclerView;

    private HomeAdapter mHomeAdapter;
    private MyBaseQuickAdapter mMyBaseQuickAdapter;
    private PagingScrollHelper mPagingScrollHelper;

    private List<String> mList = new ArrayList<>();
    private List<Integer> mIntList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        initData();
        initView();
        initEvent();
    }

    private void initEvent() {

        //这是自定义的子项
        mHomeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "你点击了" + position + "条",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("确认删除吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mHomeAdapter.removeData(position);
                            }
                        })
                        .show();
            }
        });
    }

    private void bindView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSuperRecyclerView = (SuperRecyclerView) findViewById(R.id.super_recycler_view);
    }

    private void initData() {
        for(int i = 0; i < 20; i++) {
            mList.add(i + "");
            mIntList.add(i);
        }
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager
//                (4, StaggeredGridLayoutManager.VERTICAL);               //瀑布流布局
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);           //网络布局

        mRecyclerView.setLayoutManager(layoutManager);
        mHomeAdapter = new HomeAdapter(mList, this);
        mRecyclerView.setAdapter(mHomeAdapter);
        //加入自定义分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //用PagingScrollHelper()几行代码让recyclerView实现 ViewPager的效果
        mPagingScrollHelper = new PagingScrollHelper();
        mPagingScrollHelper.setUpRecycleView(mRecyclerView);


        //下面是自带刷新的RecyclerView,这里也顺便配合BaseQuickAdapter使用
        mMyBaseQuickAdapter = new MyBaseQuickAdapter(R.layout.item_recycler, mIntList);
        mSuperRecyclerView.setAdapter(mMyBaseQuickAdapter);
        mMyBaseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //这是系统的，另外子项还可以自定义用，adapter里面有
                view.setBackgroundColor(0x66666666);
                Toast.makeText(MainActivity.this, "这是系统的，整个子项" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mSuperRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSuperRecyclerView.setOnRefreshListerner(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                randomNumber();
                mMyBaseQuickAdapter.notifyDataSetChanged();
                mSuperRecyclerView.finishLoading();
            }
        });
        mSuperRecyclerView.setOnLoadMoreListerner(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadMoreNumber();
                mMyBaseQuickAdapter.notifyDataSetChanged();
                mSuperRecyclerView.finishLoading();
            }
        });
        mSuperRecyclerView.getRecyclerView().addItemDecoration(new DividerItemDecoration
                (this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void randomNumber() {
        mIntList.clear();
        for(int i = 0; i < 20; i++) {
            mIntList.add(new Random().nextInt(3));  //0，1，2 不超过3
        }
    }

    private void loadMoreNumber() {
        for(int i = 0; i < 10; i++) {
            mIntList.add(new Random().nextInt(5)); //0-4 不超过5
        }
    }
}
