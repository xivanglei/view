package xl.gcs.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    public static Bitmap getSmallBitmap(String filePath, boolean isNeedRotate) {
        return getSmallBitmap(filePath, 768, 1024, isNeedRotate);
    }

    //按照需要的宽高尺寸，按小的那个尺寸计算比例该收缩多少（大概数值）再按比例缩放
    public static Bitmap getSmallBitmap(String filePath, int width, int height,
            boolean isNeedRotate) {
        //BitmapFactory.Options类是BitmapFactory对图片进行解码时使用的一个配置参数类
        BitmapFactory.Options options = new BitmapFactory.Options();
        //如果我们把它设为true，那么BitmapFactory.decodeFile(String path, Options opt)并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
        //否则直接返回超出范围的Bitmap会遇到OOM(Out Of Memory)的问题，内存溢出
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // 计算倍数比例，calculateInSampleSize(options, width, height)得到大的倍数，如果宽是需要的宽5倍，高是需要的高的3倍，就会得到5，
        //inSampleSize的默认值和最小值为1（当小于1时，解码器将该值当做1来处理），且在大于1时，该值只能为2的幂（当不为2的幂时，解码器会取与该值最接近的2的幂）。
        // 例如，当inSampleSize为2时，一个2000*1000的图片，将被缩小为1000*500，相应地，它的像素数和内存占用都被缩小为了原来的1/4
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        //接下来就会得到位图了
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            //得到了相对合适的图，只小不大
            bitmap = BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        } catch (OutOfMemoryError ex) {
            try {
                //如果意外溢出，就除以二返回
                options.inSampleSize = options.inSampleSize * 2;
                bitmap = BitmapFactory.decodeFile(filePath, options);
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            } catch (OutOfMemoryError ex1) {
            }
        }
        if (isNeedRotate) {
            int rotate = 0;
            try {
                //保存照片信息的类 包括拍摄的光圈、快门、平衡白、ISO、焦距、日期时间等各种和拍摄条件以及相机品牌、型号、色彩编码以及GPS等
                //比较典型的案例就是android有的机型拍照或者选择照片后，照片可能会被旋转。这个时候我们就可以通过这个类来获取图片的旋转角度信息了
                //传入文件地址获得图片信息
                ExifInterface mExifInterface = new ExifInterface(filePath);
                //TAG_ORIENTATION：旋转角度，整形表示，在ExifInterface中有常量对应表示,ExifInterface.ORIENTATION_UNDEFINED表示默认值
                int result = mExifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                switch (result) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError ex) {
            }
            if (rotate != 0 && bitmap != null) {
                //我们平常利用 Matrix 来进行 Translate（平移）、Scale（缩放）、Rotate（旋转）的操作，就是在操作着这个矩阵中元素的数值来达到我们想要的效果
                Matrix m = new Matrix();
                //设置旋转角度，后面两个参数是xy的基点位置
                m.setRotate(rotate, (float) bitmap.getWidth() / 2,
                        (float) bitmap.getHeight() / 2);
                try {
                    //2，3确定左上角位置，4，5确定长宽，6是矩阵，刚刚设定了旋转，7是，filter参数为true可以进行滤波处理，有助于改善新图像质量
                    Bitmap tempBmp = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), m, true);
                    // bmp.recycle();
                    bitmap = tempBmp;
                } catch (OutOfMemoryError ex) {
                }
            }
        }
        return bitmap;
    }

    //得到需要收缩的倍数，宽高倍数不同，就取大的，也就是图片按小的走
    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        //获取图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //如果图片宽度高度，大于我需要的宽高
        if (height > reqHeight || width > reqWidth) {
            //看看宽高大几倍，Math.round是求整，Math.round(0.50)=1，0.49 =0，-4.4 = 4， -4.6=5
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //比较宽度倍数与高度倍数，取大的倍数
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        //创建位图，Bitmap.Config.ARGB_8888是32位，质量最高
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //创建画布设置位图，画布的操作都会放在位图上
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        //画笔
        final Paint paint = new Paint();
        //声明实例化一个图片大小的矩形
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置透明度为0画布，a是透明度，0全透明，后面3位0是黑色
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        //画个圆角矩形,需要的圆角，画布大小的矩形，会显示在位图上
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        //paint.setXfermode,设置图形重叠时的处理方式，如合并，取交集或并集，经常用来制作橡皮的擦除效果。PorterDuff.Mode.SRC_IN是取两层绘制交集。显示上层。
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //挖去整块，显示在这个位置，因为设置了交集，就显示圆角的位置，最上层，就是刚画的图
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bitmap != null) {
            //回收位图资源
            bitmap.recycle();
            bitmap = null;
            //据说在Android5.0及以上手动调用System.gc()完全没必要，因为你调了也完全触发不了gc
            System.gc();
        }
        return output;
    }
    //图片缩放位64k,只是大概，不是很准确
    public static Bitmap ImageCompressL(Bitmap bitmap) {
        //Math.sqrt是开平方根，结果等于252.98
        double targetwidth = Math.sqrt(64.00 * 1000);
        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
            // 创建操作图片用的matrix对象，这里需要他的缩放方法
            Matrix matrix = new Matrix();
            // 计算宽高缩放率，Math.max是取小值，相当于如果测量宽高会超过的，以大的边为基准缩放（大的边算出来缩放比例更小，图片也更小）
            double x = Math.min(targetwidth / bitmap.getWidth(), targetwidth
                    / bitmap.getHeight());
            // 缩放图片动作，两个参数越大图片也会越大，小于1就是缩小了
            matrix.postScale((float) x, (float) x);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    //图片缩放位100k，逻辑跟上面相同
    public static Bitmap ImageCompressL2(Bitmap bitmap) {
        //Math.sqrt是开平方根
        double targetwidth = Math.sqrt(900.00 * 1000);
        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            double x = Math.min(targetwidth / bitmap.getWidth(), targetwidth
                    / bitmap.getHeight());
            // 缩放图片动作
            matrix.postScale((float) x, (float) x);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /**
     *@param
     *@描述  快速模糊化处理bitmap
     *@作者  tll
     *@时间  2016/12/5 19:22
     */
    // radius，如果填1就是不模糊，越大越模糊
    public static Bitmap fastBlur(Bitmap sentBitmap, int radius) {

        //复制图片，用原图的色彩config，isMutable 如果是true，那么产生的图片是可变的。(比如，它的像素能被修改)
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        //小于1就是不模糊，直接返回
        if (radius < 1) {
            return (null);
        }

        //获取宽高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}