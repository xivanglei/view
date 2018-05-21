package xl.gcs.com.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

/*
属性动画参数
translationX和translationY 用来沿着X或Y轴进行平移
rotation,rotationX,rotationY rotation是沿着z旋转，这里默认都是以view的中心的线为转轴
alpha是透明度
scaleX 是比例缩放
backgroundColor设置背景颜色
 */
public class MainActivity extends AppCompatActivity {
    private CustomView mCustomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slide);
        mCustomView= (CustomView) this.findViewById(R.id.customview);
        mCustomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "ddd", Toast.LENGTH_SHORT).show();
            }
        });



//        //使用属性动画使view滑动，可以让参数也跟着改变，避免下面mCustomView.setAnimation的问题
//        //沿着X从 0平移到300，如果是500，800就会先瞬移到500，再进行动画到800，实际距离移动了800，动画只播放300,参数随便几个，会移来移去，比如0,300,100,800,500
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mCustomView,"translationX",0,300);
//        //设置动画时间
//        objectAnimator.setDuration(1000);
//        //开始
//        objectAnimator.start();

        //旋转动画，rotation表示围绕z轴旋转，rotationX,rotationY就是围绕x,y轴方向,如果是xy方向后还需要平移的，转完了最好回来，否则点击倒是没问题，平移的时候就乱了
        //同样参数随便几个
//        ObjectAnimator.ofFloat(mCustomView, "rotationY", 100,0, 100, 0, 100, 0).setDuration(2000).start();

        //透明度1是完全不透明0是完全透明
//        ObjectAnimator.ofFloat(mCustomView, "alpha", 1,0, 1, 0, 1).setDuration(5000).start();

        //缩放
//        ObjectAnimator.ofFloat(mCustomView, "scaleX", 1,0, 2, 0, 1).setDuration(5000).start();

        //因为背景颜色是int类型的，所以必须是ofInt,另外必须要setEvaluator，否则会一闪一闪的,setEvaluator用于动画计算的需要，如果开始和结束的值不是基本类型的时候，这个方法是需要的。
//        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(mCustomView, "backgroundColor", 0xffff00ff, 0xffffff00, 0xffff00ff).setDuration(5000);
//        objectAnimator.setEvaluator(new ArgbEvaluator());
//        objectAnimator.start();

        //自定义属性,要在CustomView类里面加入getWidthTest告诉他如果values只有一个的时候，他是从getWidthTest里面获取默认值，再到设定的值的
        //再加入setWidthTest告诉他传入宽度后让他干什么
//        ObjectAnimator.ofInt(mCustomView, "widthTest", 200,1400,200,800).setDuration(5000).start();

        //另一种自定义属性,这里可以加入任何想动画的View，比如Button，TextView 等，然后在下面的类里自定义操作
//        MyView myView = new MyView(mCustomView);
//        final ObjectAnimator objectAnimator = ObjectAnimator.ofInt(myView, "width", 200,1400,200,800).setDuration(3000);
//        //监听动画，还可以在里面传入new AnimatorListenerAdapter()就只要复写onAnimationEnd()就好了
//        objectAnimator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                Toast.makeText(MainActivity.this, "开始咯", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                Toast.makeText(MainActivity.this, "结束了", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//                Toast.makeText(MainActivity.this, "取消了", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//                Toast.makeText(MainActivity.this, "重复播放了", Toast.LENGTH_SHORT).show();
//            }
//        });
//        //重复播放2次,一共3次
//        objectAnimator.setRepeatCount(2);
//        objectAnimator.start();
//
//        //下面是组合动画，如果上面没有注释掉，会不冲突，同时播放他播他的，我播我的，也相当于并入组合动画了，但最好是下面这种方式，好控制
//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mCustomView,"translationX",0,300);
//        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mCustomView,"alpha",0,1);
//        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(mCustomView,"rotation",0,300);
//        //组合动画需要的类
//        AnimatorSet set = new AnimatorSet();
//        //设置一次动画的时间
//        set.setDuration(3000);
////        with说明会跟之前已经有的动画一起播放（set.playTogether(a,b)也是with效果），after说明会让之前存在的动画放在新插入动画之后播放，还有before跟after相反，还可以after(long delay),设置播放完等待时间再播放
//        //下面的结果是先播3，不等待，继续同时播1，2
//        set.play(objectAnimator1).with(objectAnimator2).after(objectAnimator3);
//        set.start();

//        //这里引用单个xml中的动画,必须要建立在res的animator文件夹里面，类型必须是objectAnimator,否则不能引用
//        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.scale);
//        animator.setTarget(mCustomView);
//        animator.start();

        //这里是引用AnimatorSet(组合动画)xml的，也必须放在res的animator里面，而且类型要是set
//        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,
//                R.animator.property_animator);
//        set.setTarget(mCustomView);
//        set.start();


        //使用View动画使view滑动，相当于播放一次View的动画
//  需要注意，动画播放完如果通过xml里的fillAfter停留到最后的位置，只是视图作用，实际参数位置不会变，触摸和点击还是要点原来的点，所以最好还是让视图回来
//      mCustomView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.translate));

        //使用Scroll来进行平滑移动，需要复写方法，具体看CustomView里面
//        mCustomView.smoothScrollTo(-400,-400);
    }

    private static class MyView {
        private View mTarget;
        private MyView(View target) {
            this.mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            //这里也只是让宽度改变
            mTarget.getLayoutParams().width = width;
            //重新布局
            mTarget.requestLayout();
        }
    }
}
