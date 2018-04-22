package xl.gcs.com.tablayout;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/*
简单梳理下
要继承AppCompatActivity，不能是Activity
实现折叠至少需要4层，另外需要一个控件的行为
1层 CoordinatorLayout（实现控件的监听，单这层就可以让FloatingActionButton在需要的时候位置上移了）
2层 AppBarLayout（可以在里面包含toolbar 和其他控件，比如TabLayout，把里面的控件融为一个主题，可以让里面的控件实行动作）
3层 CollapsingToolbarLayout（继承FrameLayout，里面套toolbar和ImageView让标题栏实现折叠）
4层 Toolbar（几乎可以完全代替actionbar）,折叠或滑进滑出必须是toolbar
最后需要在配合折叠的控件上app:layout_behavior="@string/appbar_scrolling_view_behavior" />，通常是任意滚动控件，比如recyclerView，用来通知何时发生了滚动
当然需要配合AppBarLayout，而AppBarLayout的子控件需要设置app:layout_scrollFlags="scroll|exitUntilCollapsed"来配合
如果要实现折叠，这4层只能是直接子控件，不能再加一层变为5层，124层详细介绍mainActivity里面有 3层下面详细介绍
app:contentScrim="?attr/colorPrimary" 设置收缩后最顶部的颜色，如果设为透明的话，就会显示ImageView的最下面部分，实际上的动作就是imageview跑上去了，
只留下一个toolbar的高度，背景色主题色跟toolbar毫无关系，所以必须设置一个颜色把他盖住，不然看到imageView下面部分会不好看
app:expandedTitleGravity="top|right" 设置展开后标题的位置默认是bottom|left
app:title="地方大幅度发" 设置标题，也可以collapsingToolbar.setTitle("哆啦A梦");来设置标题，折叠toolbar后，不管展开收回，toolbar的标题就没用了
但是toolbar的其他东西都有用，比如home键 和菜单按钮
app:collapsedTitleGravity="center" 收回去的标题位置，默认是left
app:expandedTitleMarginEnd="64dp" 标题的位置偏移
app:expandedTitleMarginStart="48dp" 标题的位置偏移
app:layout_scrollFlags="scroll|exitUntilCollapsed" 设置滚动事件，这里设置过了以后toolbar就不用设了，scroll可滑动，exitUntilCollapsed是折叠，如果改为enterAlways就是滑进滑出了，不留一部分了
recyclerView的android:scrollbars="none" 表示滚动条,可以vertical horizontal none
下面的LinearLayout 中app:layout_behavior="xl.gcs.com.tablayout.FooterBehaviorAppBar"表示这是个自定义的Behavior类


ViewPager检测到NestedScrolling相关的事件触发，就不做拦截，让外层的CoordinatorLayout成功监听到ViewPager嵌套的RecyclerView或NestedScrollView的滑动事件。
CoordinatorLayout目前我仅在RecyclerView、NestedScrollView、ViewPager上面成功通过设置behavior来监听滑动事件
 */
public class CollapsingToolbarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("哆啦A梦");
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new RecyclerViewAdapter(CollapsingToolbarActivity.this));
        final ActionBar ab = getSupportActionBar();
        //为左上角的home键设置图标，默认是左箭头图标
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        //让左上角home 键显示出来
        ab.setDisplayHomeAsUpEnabled(true);
        //可以加图片也可以加frameLayout，里面还可以放东西，设置点击，展开就能点，折叠就会藏住点不到
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CollapsingToolbarActivity.this, "随便测试下", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //让菜单显示出来
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    /*
    下面介绍Fragment里面展现toolbar菜单的方法，折叠标题栏在fragment里能用

    获取activity设置标题

((MainActivity) getActivity()).setSupportActionBar(mToolbar);
    //  得到titlebar
    ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
    检查菜单，让菜单显示出来，fragment是不会主动让菜单显示的
    setHasOptionsMenu(true);
//让左上角home 键显示出来
ab.setDisplayHomeAsUpEnabled(true);
//为左上角的home键设置图标，默认是左箭头图标
ab.setHomeAsUpIndicator(R.mipmap.back);
//设置收缩展开标题必须Java里，xml里没有方法
mCollapsingToolbarLayout.setCollapsedTitleTextColor(0xff000000);
    //菜单点击跟activity一样
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //home键
            case android.R.id.home:

                break;

            //通知
            case R.id.menu_notification:
                startActivity(new Intent(getActivity(), MessageListActivity.class));

                break;
            default:

                break;
        }
        return true;
    }
    //让菜单显示出来的方法有点区别
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.menu_setting, menu);
    }
    */

}
