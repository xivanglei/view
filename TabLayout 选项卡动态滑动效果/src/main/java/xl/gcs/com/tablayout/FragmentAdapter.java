package xl.gcs.com.tablayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.List;

/**
 * Created by xianglei on 2018/4/15.
 * FragmentStatePagerAdapter 给ViewPager 用的Fragment类型的adapter
 *
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;
    private List<String> mTitles;
    private Context mContext;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles, Context context) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
        mContext = context;
    }

    //返回fragment
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    //数量
    @Override
    public int getCount() {
        return mFragments.size();
    }

    //返回标题,不是必须设置的，只有带标题才需要返回，没标题可以把这方法删除，但一旦设了这方法，标题数就要跟fragment一样多
    //标题少于碎片，就奔溃，标题多于碎片后面标题没用起来
    @Override
    public CharSequence getPageTitle(int position) {

                return mTitles.get(position);

                //下面是介绍TabLayout的图片标题格式
//        Drawable drawable;
//        switch (position) {
//            case 0:
//                drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_launcher_background);
//                break;
//            case 1:
//                drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_launcher_foreground);
//                break;
//            case 2:
//                drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_menu);
//                break;
//            default:
//                drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_xuanfeng);
//                break;
//        }
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());  //获取的宽度和高度放在参3，参4
//        //思路应该是必须返回字符串，但我们就返回图文混排的，不设文字，等于就返回了图片
//        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
//        SpannableString spannableString = new SpannableString(" ");     //文字一定不能设定，否则就只有字了
//        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);//这句不是很懂
//
//        return spannableString;
    }
}
