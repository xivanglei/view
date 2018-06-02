package xl.gcs.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//Glide可以加载 gif图，都不需要做判断
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.img_view)
    ImageView img_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_load_image})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_load_image:
                String url = "http://p1.pstatp.com/large/166200019850062839d3";
                Glide.with(this)
                        .load(url)
                        //只允许加载静态图片，如果不加这行就是，自动判断图片格式，都能显示
                        .asBitmap()
                        //占位图就是指在图片的加载过程中，我们先显示一张临时的图片，等图片加载出来了再替换成要加载的图片
                        .placeholder(R.drawable.ic_launcher_foreground)
                        //出现异常占位图
                        .error(R.drawable.ic_launcher_background)
                        //这里串接了一个diskCacheStrategy()方法，并传入DiskCacheStrategy.NONE参数，这样就可以禁用掉Glide的缓存功能
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        //指定大小，正常是不需要指定大小的，Glide会自动判断图所需大小，就加载多大，不造成内存浪费
                        .override(100, 100)
                        .into(img_view);

                //下面几种加载方法是可行的，但没有提供相应的图片，只看看方法，不能运行
                // 加载本地图片
//                File file = new File(getExternalCacheDir() + "/image.jpg");
//                Glide.with(this).load(file).into(img_view);
                // 加载应用资源
//                int resource = R.drawable.ic_launcher_background;
//                Glide.with(this).load(resource).into(img_view);
                // 加载二进制流
//                byte[] image = getImageBytes();
//                Glide.with(this).load(image).into(img_view);
                // 加载Uri对象
//                Uri imageUri = getImageUri();
//                Glide.with(this).load(imageUri).into(img_view);
                break;
        }
    }
}

