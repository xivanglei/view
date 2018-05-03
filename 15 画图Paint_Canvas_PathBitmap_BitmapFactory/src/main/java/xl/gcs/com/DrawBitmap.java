package xl.gcs.com;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by xianglei on 2018/4/25.
 *Drawable：通用的图形对象，用于装载常用格式的图像，既可以是PNG，JPG这样的图像， 也是前面学的那13种Drawable类型的可视化对象！我们可以理解成一个用来放画的——画框！
 Bitmap(位图)：我们可以把他看作一个画架，我们先把画放到上面，然后我们可以 进行一些处理，比如获取图像文件信息，做旋转切割，放大缩小等操作！
 Canvas(画布)：如其名，画布，我们可以在上面作画(绘制)，你既可以用Paint(画笔)， 来画各种形状或者写字，又可以用Path(路径)来绘制多个点，然后连接成各种图形！
 Matrix(矩阵)：用于图形特效处理的，颜色矩阵(ColorMatrix)，还有使用Matrix进行图像的 平移，缩放，旋转，倾斜等！
 *
 * Bitmap的构造方法是私有的，外面不能实例化，只能通过JNI实例化！ 当然，肯定也会给我们提供一个接口给我们来创建Bitmap的，而这个接口类就是：BitmapFactory
 * •decodeByteArray(byte[] data, int offset, int length)：从指定的字节数组的offset位置开始，将长度为length的字节数据解析成Bitmap对象。
 •decodeFile(String pathName)：从pathName指定的文件中解析、创建Bitmap对象。
 •decodeFileDescriptor(FileDescriptor fd)：从FileDescriptor对应的文件中解析、创建Bitmap对象。
 •decodeResource(Resources res, int id)：根据给定的资源ID从指定资源中解析、创建Bitmap对象。
 •decodeStream(InputStream is)：从指定的输入流中解析、创建Bitmap对象。
 public boolean compress (Bitmap.CompressFormat format, int quality, OutputStream stream) 将位图的压缩到指定的OutputStream，可以理解成将Bitmap保存到文件中！ format：格式，PNG，JPG等； quality：压缩质量，0-100，0表示最低画质压缩，100最大质量(PNG无损，会忽略品质设定) stream：输出流 返回值代表是否成功压缩到指定流！
 void recycle()：回收位图占用的内存空间，把位图标记为Dead
 boolean isRecycled()：判断位图内存是否已释放
 int getWidth()：获取位图的宽度
 int getHeight()：获取位图的高度
 boolean isMutable()：图片是否可修改
 int getScaledWidth(Canvas canvas)：获取指定密度转换后的图像的宽度
 int getScaledHeight(Canvas canvas)：获取指定密度转换后的图像的高度
 静态方法：
 Bitmap createBitmap(Bitmap src)：以src为原图生成不可变得新图像
 Bitmap createScaledBitmap(Bitmap src, int dstWidth,int dstHeight, boolean filter)：以src为原图，创建新的图像，指定新图像的高宽以及是否变。
 Bitmap createBitmap(int width, int height, Config config)：创建指定格式、大小的位图
 Bitmap createBitmap(Bitmap source, int x, int y, int width, int height)以source为原图，创建新的图片，指定起始坐标以及新图像的高宽。
 下面 参数6，filter参数为true可以进行滤波处理，有助于改善新图像质量;flase时，计算机不做过滤处理，参数5 Matrix就是矩阵，我们平常利用 Matrix 来进行 Translate（平移）、Scale（缩放）、Rotate（旋转）的操作，就是在操作着这个矩阵中元素的数值来达到我们想要的效果
 public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)
 Bitmap.Config.ARGB_8888：4个字节，a = 8，r = 8，g = 8， b = 8，一个像素点 8 + 8 + 8 + 8 = 32，  ALPHA_8：8位位图；1 个字节，只有透明度，没有颜色值，
 ARGB_4444：2 个字节，a = 4，r = 4，g = 4，b = 4，一个像素点 4+4+4+4 = 16，  RGB_565：2 个字节，r = 5，g = 6，b = 5，一个像素点 5+6+5 = 16
 位图位数越高代表其可以存储的颜色信息越多，当然图像也就越逼真。使用RGB_565会比使用ARGB_8888少消耗2倍的内存，很多时候默认是ARGB_8888，所以我们需要主动设置为RGB_565.
 */

public class DrawBitmap extends View {

    public DrawBitmap(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        //从文件中获取位图
//        String path = Environment.getExternalStorageDirectory() + "/test/img03.jpg";
//        Bitmap bm = BitmapFactory.decodeFile(path);
        //从资源文件中获取位图
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //画位图
        float height = bm.getHeight();
        float width = bm.getWidth();
//        canvas.drawBitmap(bm, 0, 330, paint);
        //建两个矩形
        Rect src = new Rect(30, 30, 100, 100);
        Rect dst = new Rect(350, 90, 465, 230);
        //从bm上的src矩形位置挖去一块（图片左上角为原点）,显示到dst矩形上，如果显示的矩形尺寸跟挖去的不一样，就会缩放，导致比例大小不等
//        canvas.drawBitmap(bm, src, dst, paint);
        //工具类存图
//        SDCardHelper.saveBitmapToSDCardPrivateCacheDir(bm, "ssss.png");
        //工具类读取图片，太大会改小再返回，比要求的小就不变返回，后面参数可旋转,需要权限，需要权限
        Bitmap bitmap = BitmapUtil.getSmallBitmap(SDCardHelper.getSDCardPublicDir("img03.jpg"), false);
        //获得圆角位图
//        Bitmap aaa = BitmapUtil.getRoundedCornerBitmap(bitmap,80);
        //把图按照大概100k显示
//        Bitmap aaa = BitmapUtil.ImageCompressL(bitmap);
        //储存图片
//        SDCardHelper.saveBitmapToSDCardPrivateCacheDir(aaa, "ssss.png");
        //模糊图片
        Bitmap aaa = BitmapUtil.fastBlur(bitmap, 20);
        canvas.drawBitmap(aaa, 0, 330, paint);
        Log.d(TAG, "onDraw: " + aaa.getHeight());
        Log.d(TAG, "onDraw: " + aaa.getWidth());
        File file = new File(SDCardHelper.getSDCardPrivateFilesDir("ACache"), "abc".hashCode() + "");
        Log.d(TAG, "onDraw: " + file.getAbsolutePath());
        BufferedWriter out = null;
        try {
            //用于写入字符文件的便捷类。此类的构造方法假定默认字符编码和默认字节缓冲区大小都是可接受的。
            out = new BufferedWriter(new FileWriter(file), 1024);

            out.write("试试能不能存");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
