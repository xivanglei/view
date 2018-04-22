package xl.gcs.com.tablayout;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
implementation 'com.android.support:recyclerview-v7:26.1.0'
    implementation 'com.android.support:design:26.1.0'
    implementation 'com.android.support:cardview-v7:26.1.0'

    CoordinatorLayout可以实现子控件的折叠，监听控件的事件，让FloatingActionButton偏移，配合AppBarLayout(直接子控件)就能让toolbar滑进滑出了，布局方式就当做Fragment就可以了
    AppBarLayout继承自LinearLayout，布局方向为垂直方向。所以你可以把它当成垂直布局的LinearLayout来使用。
    AppBarLayout是在LinearLayout上加了一些材料设计的概念，它可以让你定制当某个可滚动View的滚动手势发生变化时，其内部的子View实现何种动作，
    一些功能要配合CoordinatorLayout，要让toolbar实现折叠或滑进滑出就要配合CoordinatorLayout了。
    toolBar 的 这句app:layout_scrollFlags="scroll|enterAlways" ,必须配合CoordinatorLayout在最外层，AppBarLayout中间层
    toolBar里层，3层合起来就能配合其他滚动控件使用了，可以跟着隐藏和浮现，scroll表示可以滑，enterAlways好像表示先后，还可以跟|snap表示移动到一半的时候，根据位置判断隐藏还是显示
    viewPager的 app:layout_behavior="@string/appbar_scrolling_view_behavior" 可以让里面的recyclerView配合CoordinatorLayout实现折叠,
    有了这行，就等于可以被CoordinatorLayout监控了，他就知道你滑动了多少，让他的子控件实现滑动或折叠的动作，还可以让自定义的behavior控件有动作，
    自定义控件类继承CoordinatorLayout.Behavior<View>，之后在任意控件里加app:layout_behavior="xl.gcs.com.tablayout.FooterBehaviorAppBar就好了
    比如LinearLayout都正常写，最后一行加app:layout_behavior="xl.gcs.com.tablayout.FooterBehaviorAppBar，就能实现滑进滑出效果了

    TabLayout 的 app:tabMode="scrollable" 可以让这控件滑动 如果设为fixed就全部显示，会造成显示不下就压缩显示

    android:fitsSystemWindows=“true” 默认行为就是通过在 View 上设置和系统窗口一样高度的边框（padding ）来确保你的内容不会出现到系统窗口下面。
    简单点说，就是跟状态栏融合该空间会出现在状态栏里，但高度超出的话状态栏会变高
    FloatingActionButton的 app:layout_anchor="@id/main_content"让他放在@id/main_content里，试了下不能放CoordinatorLayout里，放AppBarLayout,会一半在里面一半在外面，
    recyclerView滑的时候，如果AppBarLayout能滑还会跟着划上划下，放在其他控件里都算正常，也能放toolbar里面
    app:layout_anchorGravity="bottom|right|end" 摆放位置

    内容太多，折叠就下一个页面介绍好了
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    //配合AppBarLayout只是让TabLayout颜色主题跟标题一直，没这需求，可以不用套AppBarLayout，
    // 加了app:tabMode="scrollable"可以让显示不下的东西显示到外面，到时滑进来,如果项目不多就设为fixed，就会平均分
    //但如果显示的项目太少就不能滑只能点，或滑动下面的页面
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        //为左上角的home键设置图标，默认是左箭头图标
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        //让左上角home 键显示出来
        ab.setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_navigation);
        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //下面这句是把菜单子项设为可选中，选中后就会改变背景色，对于这里的菜单上面部分由于包含在group里，所以本来就是可选的
                    //加上这句就把下面的不是group里的菜单也设为可选
                    item.setCheckable(true);
                    String title = item.getTitle().toString();
                    Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                    //可以专门设置关闭哪个方向的drawerLayout 也可以closeDrawers();关闭全部
                    mDrawerLayout.closeDrawer(Gravity.START);
                    return true;
                }
            });
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        initViewPager();
    }

    private void initViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("精选");
        titles.add("体育");
        titles.add("巴萨");
        titles.add("购物");
//        titles.add("明星");
//        titles.add("视频");
//        titles.add("健康");
//        titles.add("励志");
//        titles.add("图文");
//        titles.add("本地");
//        titles.add("动漫");
//        titles.add("搞笑");
//        titles.add("精选");

        for (int i = 0; i < titles.size(); i++) {
            //这里这句应该是用不上的，不管加或不加，或加错标题，标题在FragmentAdapter中都会返回，目前找不到用处
            //试过不联动不返回的话也不会显示出标题
//            mTabLayout.addTab(mTabLayout.newTab().setText("嗯嗯"));
        }
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            fragments.add(new ListFragment());
        }
        FragmentAdapter mFragmentAdapteradapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles, this);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来，不关联起来，就会各管各的，上面也能移动，下面也能滑动，但不联动。
        mTabLayout.setupWithViewPager(mViewPager);

        //style="@style/MyCustomTabLayout" 可以设置自定义style


//        //给TabLayout设置适配器 这句好像也没用
//        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapteradapter);
    }

    //给菜单设置点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //系统的左上角的home键
            case android.R.id.home:
                //打开DrawerLayout,START方向
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //可以当做普通的button一样监听，这里为了方便
    public void checkin(View view) {
        //snackbar的用法第6篇有介绍
        Snackbar.make(view, "点击成功", 2000).show();
    }
}

