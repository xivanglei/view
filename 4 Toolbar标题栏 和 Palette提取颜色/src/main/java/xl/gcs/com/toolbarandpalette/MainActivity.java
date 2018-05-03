package xl.gcs.com.toolbarandpalette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/*
toolbar 不用引入
palette implementation 'com.android.support:palette-v7:26.1.0'

@android:style/Theme.Black//背景黑色-有标题-非全屏
@android:style/Theme.Black.NoTitleBar //背景黑色-无标题-非全屏
@android:style/Theme.Black.NoTitleBar.Fullscreen //背景黑色-无标题-全屏显示
@android:style/Theme.Light//背景白色-有标题-非全屏
@android:style/Theme.Light.NoTitleBar //背景白色-无标题-非全屏
@android:style/Theme.Light.NoTitleBar.Fullscreen //背景白色-无标题-全屏显示
@android:style/Theme.Light.Panel
@android:style/Theme.Light.WallpaperSettings //背景透明
@android:style/Theme.NoDisplay
@android:style/Theme.Translucent.NoTitleBar.Fullscreen //半透明、无标题栏、全屏
@android:style/Theme.Wallpaper.NoTitleBar.Fullscreen

colorPrimary 应用的主要色调，actionBar默认使用该颜色，Toolbar导航栏的底色
colorPrimaryDark 应用的主要暗色调，statusBarColor默认使用该颜色
statusBarColor 状态栏颜色，默认使用colorPrimaryDark
windowBackground 窗口背景颜色
navigationBarColor 底部栏颜色
colorForeground 应用的前景色，ListView的分割线，switch滑动区默认使用该颜色
colorBackground 应用的背景色，popMenu的背景默认使用该颜色
colorAccent CheckBox，RadioButton，SwitchCompat等一般控件的选中效果默认采用该颜色
colorControlNormal CheckBox，RadioButton，SwitchCompat等默认状态的颜色。
colorControlHighlight 控件按压时的色调
colorControlActivated 控件选中时的颜色，默认使用colorAccent
colorButtonNormal 默认按钮的背景颜色
editTextColor ：默认EditView输入框字体的颜色。
textColor Button，textView的文字颜色
textColorPrimaryDisableOnly RadioButton checkbox等控件的文字
textColorPrimary 应用的主要文字颜色，actionBar的标题文字默认使用该颜色
colorSwitchThumbNormal switch thumbs 默认状态的颜色. (switch off)

菜单中android:orderInCategory="80" 指的是拜访顺序，从低到高
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView tv_close;
    private TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        initViews();
    }

    private void initViews() {
        tv_close= (TextView) this.findViewById(R.id.tv_close);
        mToolbar= (Toolbar) this.findViewById(R.id.toolbar);
        tv_content = (TextView) findViewById(R.id.tv_content);
        mToolbar.setTitle("Toolbar");
        //如果不把toolbar设为actionBar 就不会显示toolbar的内容，但控件的位置还在，设多高，还有有多高的主题色留下来
        setSupportActionBar(mToolbar);
        //是否给左上角图标的左边加上一个返回的图标，其实书里下面这句重复了，因为actionBarDrawerToggle会自动显示出图标的。
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //可以设置图标getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        //可以设置副标题，在主标题下面。getSupportActionBar().setSubtitle("dkddd");
        //给标题设置图标
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        //给菜单设置监听的一种方式，另一种方式在下面
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                        break;
                        //不用设置search,搜索是调用系统的，app:actionViewClass="android.support.v7.widget.SearchView"
                    default:
                        break;
                }
                return true;
            }
        });

        //设置侧或布局
        mDrawerLayout= (DrawerLayout) this.findViewById(R.id.id_drawerlayout);
        //ActionBarDrawerToggle 1.改变android.R.id.home返回图标。2.Drawer拉出、隐藏，带有android.R.id.home动画效果。3.监听Drawer拉出、隐藏；
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //侧滑界面里面的textView
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        //使用Patette
        setPatette();
    }

    private void setPatette() {
        //获取图片
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.bitmap);
        //从这图片里获取主题色调，为其他控件设置主题背景色
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch=palette.getVibrantSwatch();
                //设置actionBar的背景色
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));

                //也可以把某一控件的背景色设置掉
//                tv_content.setBackgroundDrawable(new ColorDrawable(swatch.getRgb()));
            }
        });
    }

    //让菜单显示出来
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //找到菜单，可以在代码里动态更改一些东西
        MenuItem menuItem = menu.findItem(R.id.ab_search);
        //设置菜单图标
        menuItem.setIcon(R.drawable.bitmap);
        return true;
    }


    //菜单的点击事件的另一种方式,上面设置过监听的以上面为准，下面有关菜单部分的监听就全部失效了。
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //这是android自带的id 指的是左上角的返回，当然图标可以更改，home键只能在这里监听，不能按照上面的方法监听
            case android.R.id.home:
                //打开菜单，方向是开始方向
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
                //第一种方式注册过监听，下面两个按钮即使上面没监听回调，也只能在上面写回调了，这里写了没用
            //如果没有注册过，只通过这个方法注册，可以有效
            case R.id.action_share:
                Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}

