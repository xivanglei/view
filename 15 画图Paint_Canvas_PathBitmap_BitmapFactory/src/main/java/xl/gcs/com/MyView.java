package xl.gcs.com;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xianglei on 2018/1/19.
 * paint 设置文字的方法
 *public void setTextSize(float textSize):设置绘制文字的大小，必须大于 0。
 public void setTextAlign(Align align):设置绘制文字的对齐方向，取值有 Paint.Align.LEFT、Paint.Align.CENTER、Paint.Align.RIGHT。
 public void setTextLocale(@NonNull Locale locale):API 17 出现的，设置地理位置，一般用 Locale.getDefault() 即可，
 我在学习的时候分别设置了 Locale.CHINESE 、 Locale.ENGLISH 、 Locale.JAPANESE，区别就是字体不太一样，并不是我想象中的自动翻译。
 public void setTextLocales(@NonNull @Size(min=1) LocaleList locales):API 24 出现的，设置地理位置组。
 public void setTextScaleX(float scaleX):设置绘制文字 x 轴的缩放比例，即文字的拉伸效果。默认值为 1，大于 1 则拉伸，大于 0 且小于 1 则收缩，小于 0 的时候会有意想不到的效果。
 public void setTextSkewX(float skewX):设置绘制文字倾斜度，默认值为 0，取值范围貌似是 -48.0f ~ 48.0f。
 public void setLinearText(boolean linearText):设置绘制文字是否需要缓存，true 为不需要。
 public void setSubpixelText(boolean subpixelText):当设置为 true 时，它可以保证在绘制斜线时平滑该斜线的外观，相当于绘制文字的抗锯齿效果。
 public void setUnderlineText(boolean underlineText):设置绘制文字是否带有下划线。
 public void setElegantTextHeight(boolean elegant):API 21 出现的，设置绘制文字是否具有优雅的文字高度，但是具体有什么效果还有试出来。
 public void setFakeBoldText(boolean fakeBoldText):设置绘制文字是否为粗体字，当字体大小比较小时效果会非常差。
 public void setStrikeThruText(boolean strikeThruText):设置绘制文字是否带有删除线。
 public void setTypeface(Typeface typeface):设置字体，常用字体：DEFAULT 、 DEFAULT_BOLD 、 SANS_SERIF 、 SERIF 、 MONOSPACE。
 设置字体代码如下，各字体的效果大家可以自己试下：
 Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
 mPaint.setTypeface(typeface);
 paint 设置图形的方法
 public void setARGB(int a, int r, int g, int b):设置画笔透明度及颜色，各参数取值范围都是0~255。
 public void setAlpha(int a):设置画笔透明度，取值范围为0~255，255为完全不透明。
 public void setColor(Color color):设置画笔颜色。
 public void setAntiAlias(boolean aa):设置抗锯齿，设置为 false 会出现锯齿状的边界，设置为 true 会多消耗性能但边界就会变得模式，避免锯齿状的情况。
 public void setDither(boolean dither):防抖动，这个属性的需求场景主要出现在绘制渐变色彩或含渐变的图片时，Android 对不含 alpha 通道的图片会进行一个转化，成为 RGB565 格式的，这种格式占用内存小，但因为如此，就会出现讨厌的“色带”情景，让人感觉过渡的不是那么柔和，针对这个问题，Android 提出了防抖动，设置为 true ，它会将原始颜色的过渡处根据两边的色值进行一些改变，从而让颜色过渡更加的柔和，让人觉得是平滑的过渡。
 public void setStyle(Style style):设置画笔样式，可选参数有 Paint.Style.FILL(填充)、 Paint.Style.STROKE(描边)、 Paint.Style.FILL_AND_STROKE(填充及描边)。
 public void setStrokeCap(Cap cap):设置画笔帽，可选参数有 Paint.Cap.BUTT(无笔帽)、 Paint.Cap.ROUND(圆形笔帽)、 Paint.Cap.SQUARE(方形笔帽)。笔帽的意思就是前后两端多出来一截。
 public void setStrokeJoin(Join join):这个方法用于设置接合处的形态，就像你用代码画了一条线，但是这条线其实是由无数条小线拼接成的，拼接处的形状就由该方法指定。可选参数是：Paint.Join.BEVEL(直线)、 Paint.Join.MITER（锐角）、Paint.Join.ROUND（圆弧）。但其实 Paint.Join.Round 和 Paint.Join.BEVEL 并没有明显的区别。
 public void setStrokeWidth(float width):当画笔样式为 STROKE 或 FILL_AND_STROKE 时，设置画笔的宽度。
 public void setFilterBitmap(boolean filter):如果该项设置为 true ，则图像在动画进行中会滤掉对 Bitmap 图像的优化操作，加快显示速度，本设置项依赖于 dither 和 xfermode 的设置。
 public void setShadowLayer(float radius, float dx, float dy, int shadowColor):在图形下面设置阴影层，产生阴影效果，radius 为阴影的角度，dx 和 dy 为阴影在 x 轴和 y 轴上的距离，color 为阴影的颜色。
 public void setShader(Shader shader):设置图像效果，使用 Shader 可以绘制出各种渐变效果。
 public Xfermode setXfermode(Xfermode xfermode):设置图形重叠时的处理方式，如合并，取交集或并集，经常用来制作橡皮的擦除效果。
 1.PorterDuff.Mode.CLEAR，所绘制不会提交到画布上，也就是不显示内容。2.PorterDuff.Mode.SRC，显示绘制图片的上层图片。
 3.PorterDuff.Mode.DST，显示绘制图片下层图片。4.PorterDuff.Mode.SRC_OVER，正常绘制显示，上下层绘制叠盖。
 5.PorterDuff.Mode.DST_OVER，上下层都显示，下层居上显示。6.PorterDuff.Mode.SRC_IN，取两层绘制交集。显示上层。
 7.PorterDuff.Mode.DST_IN，取两层绘制交集。显示下层。8.PorterDuff.Mode.SRC_OUT，取上层绘制非交集部分。
 9.PorterDuff.Mode.DST_OUT，取下层绘制非交集部分。10.PorterDuff.Mode.SRC_ATOP，取下层非交集部分与上层交集部分。
 11.PorterDuff.Mode.DST_ATOP，取上层非交集部分与下层交集部分。12.PorterDuff.Mode.XOR，异或：去除两图层交集部分。
 13.PorterDuff.Mode.DARKEN，取两图层全部区域，交集部分颜色加深。14.PorterDuff.Mode.LIGHTEN，取两图层全部，点亮交集部分颜色。
 15.PorterDuff.Mode.MULTIPLY，取两图层交集部分叠加后颜色。16.PorterDuff.Mode.SCREEN，取两图层全部区域，交集部分变为透明色。
 public void setMaskFilter(MaskFilter maskfilter):设置 MaskFilter ，可以用不同的 MaskFilter 实现滤镜的效果，如滤化，立体等。
 public void setColorFilter(ColorFilter colorfilter):设置颜色过滤器，可以在绘制颜色时实现不用颜色的变换效果。
 public void setPathEffect(PathEffect effect):设置绘制路径的效果，如点画线等。

 canvas的常用方法
 public void translate(float dx, float dy):平移画布，dx 为在 x 轴上平移的距离，dy 为在 y 轴上平移的距离。
 public final void scale(float sx, float sy):缩放画布，sx 为 x 轴缩放的倍数，sy 为 y 轴缩放的倍数。
 public final void scale(float sx, float sy, float px, float py):缩放画布，sx、sy 同上，px 为 x 轴的基准点，py 为 y 轴的基准点，默认情况是以原点为基准点，这个方法可以以某个中心点来进行缩放。
 public void rotate(float degrees):旋转画布，degrees 为旋转的角度，正值为顺时针旋转，负值为逆时针旋转。
 public final void rotate(float degrees, float px, float py):类似 scale() 方法，可以指定基准点。
 public void skew(float sx, float sy):倾斜画布，sx 为将画布在 x 轴上倾斜相应的角度的 tan 值，sy 为将画布在 y 轴上倾斜相应的角度的 tan 值。
 public int save():保存画布当前状态。
 public int save(@Saveflags int saveFlags):保存画布当前状态到指定位置。
 public void restore():恢复画布状态至上一次保存的状态。
 public int getSaveCount():返回画布保存的状态个数。
 public void restoreToCount(int saveCount):恢复画布状态至指定位置的状态。
 public void drawRGB(int r, int g, int b):使用 RGB 填充画布。
 public void drawARGB(int a, int r, int g, int b):使用 ARGB 填充画布。
 public void drawColor(@ColorInt int color):使用 Color 填充画布。
 public void drawPaint(@NonNull Paint paint):使用指定 paint 填充画布。
 public void drawPoints(@Size(multiple=2) @NonNull float[] pts, @NonNull Paint paint):画多个点，传入坐标数组，数组中每两个值为一组，为一个点的坐标，如果最后不足两个则忽略。
 public void drawPoints(@Size(multiple=2) float[] pts, int offset, int count, @NonNull Paint paint):画多个点，传入坐标数组，数组中每两个值为一组，为一个点的坐标，如果最后不足两个，则忽略,offset 为截取的取值范围起始位置(包含),count 为截取的取值范围的结束位置(包含)。
 public void drawPoint(float x, float y, @NonNull Paint paint):画一个点，x 为 x 轴坐标，y 为 y 轴坐标。
 public void drawLine(float startX, float startY, float stopX, float stopY, @NonNull Paint paint):画一条线，startX 为 x 轴起点坐标，startY 为 y 轴起点坐标，stopX 为 x 轴终点坐标，stopY 为 y 轴终点坐标。
 public void drawLines(@Size(multiple=4) @NonNull float[] pts, @NonNull Paint paint):画多条线，同 public void drawPoints(@Size(multiple=2) @NonNull float[] pts, @NonNull Paint paint)，不过数组中是每四个值为一组，如果最后不足四个则忽略。
 public void drawLines(@Size(multiple=4) @NonNull float[] pts, int offset, int count, @NonNull Paint paint):画多条线，同 public void drawPoints(@Size(multiple=2) float[] pts, int offset, int count, @NonNull Paint paint)，不过数组中是每四个值为一组，如果最后不足四个则忽略。
 public void drawRect(float left, float top, float right, float bottom, @NonNull Paint paint):画矩形，参数分别为矩形的左边、顶边、右边和底边。
 public void drawRect(@NonNull Rect r, @NonNull Paint paint):画矩形，r 为 int 类型参数创建的矩形。
 public void drawRect(@NonNull RectF rect, @NonNull Paint paint):画矩形，rect 为 float 类型参数创建的矩形。
 public void drawOval(float left, float top, float right, float bottom, @NonNull Paint paint):API 21 出现的，画椭圆，参数分别为矩形的左边、顶边、右边和底边。
 public void drawOval(@NonNull RectF oval, @NonNull Paint paint):画椭圆，oval 为 float 类型参数创建的矩形。
 public void drawCircle(float cx, float cy, float radius, @NonNull Paint paint):画圆，cx 为圆心在 x 轴的坐标，cy 为圆心在 y 轴的坐标，radius 为圆的半径。
 public void drawArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint):API 21 出现的，画扇形或者弧线，参数分别为矩形的左边、顶边、右边、底边，开始角度，扇形的角度，是否需要和圆心连线。
 public void drawArc(@NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint):oval 为 float 类型参数创建的矩形，其余参数同上。
 public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, @NonNull Paint paint):API 21 出现的，参数分别为矩形的左边、顶边、右边、底边，椭圆的位于 x 轴的圆角的半径，椭圆的位于 y 轴的圆角的半径。
 public void drawRoundRect(@NonNull RectF rect, float rx, float ry, @NonNull Paint paint):rect 为 float 类型参数创建的矩形。
 public void drawPath(@NonNull Path path, @NonNull Paint paint):画路线，也可以利用该方法来画任意多边形，参数 path 为路线对象。
 */

public class MyView extends View {
    Paint mPaint;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //画虚线直线需要加这条，关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        //渐变效果x0表示渲染起始位置的x坐标，y0表示渲染起始位置的y坐标，x1表示渲染结束位置的x坐标，y1表示渲染结束位置的y坐标，colors表示渲染的颜色，它是一个颜色数组，数组长度必须大于等于2，positions表示colors数组中几个颜色的相对位置，是一个float类型的数组，该数组的长度必须与colors数组的长度相同。如果这个参数使用null也可以，这时系统会按照梯度线来均匀分配colors数组中的颜色，最后一个参数则表示平铺方式，有三种
        Shader shader = new LinearGradient(130, 130, 50, 50,
                Color.RED, Color.GREEN, Shader.TileMode.MIRROR);
        //设置图像效果
        paint.setShader(shader);
        //画矩形，上下左右坐标加画笔
        canvas.drawRect(10, 10, 280, 150, paint);

        mPaint = new Paint();
        //设置画笔颜色
        mPaint.setColor(Color.RED);
        //设置画笔描边宽度
        mPaint.setStrokeWidth(4);
        //画直线，前两个为起点xy,后两个终点xy,加画笔
        canvas.drawLine(0, 100, 800, 100, mPaint);

        mPaint.setColor(Color.YELLOW);
        //先储存画布的状态，主要针对平移、放缩、旋转、错切、裁剪等操作，怕到时会有影响，可以恢复
        canvas.save();
        //缩放画布，x轴缩放倍数和y轴缩放倍速，之前画好的不变，之后的按倍数来画，0.5表示差不多长短粗细都减半，下面4个参数都除以二（所以位置也会变）,线宽也除以二
        canvas.scale(0.5f, 0.5f);
        canvas.drawLine(0, 100, 800, 100, mPaint);

        //恢复到上次的save()状态，主要针对平移、放缩、旋转、错切、裁剪等操作，不能把画好的东西给消了,一次save()对应一次restore()
        canvas.restore();
        canvas.save();
        mPaint.setColor(Color.BLUE);
        //指定原点缩放，400是x,50是y
        canvas.scale(0.5f, 0.5f, 400, 50);
        canvas.drawLine(0, 100, 800, 100, mPaint);
        canvas.restore();
        //重置画笔
        mPaint.reset();
        canvas.save();
        //Canvas 的 x 轴平移 100dx,y 轴平移 100dx.
        canvas.translate(100, 100);
        mPaint.setColor(Color.YELLOW);
        canvas.drawLine(0, 110, 800, 110, mPaint);
        canvas.restore();
        canvas.save();
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.BLUE);
//        Canvas 顺时针旋转 70 度。可以这样理解，先让画布按逆时针的角度旋转70°，画完了再回来，注意，基点是原点，如果画图不是从原点开始画，位置会变掉
        canvas.rotate(70);
        canvas.drawLine(0, 100, 800, 100, mPaint);
        canvas.restore();
        canvas.save();
        //让画布绕指定点转70°,可以解决不在原点画图位置变化问题
        canvas.rotate(70, 0, 100);
        canvas.drawLine(0, 100, 800, 100, mPaint);
        canvas.restore();
        mPaint.reset();


        //接下来画图形,先画个半透明背景
        canvas.drawColor(0x77ffffff);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(40);
        //设置笔帽为圆形，结合处的形状，如果不设置的话，用宽度大的笔画出来的点事正方形的，设置圆形后就是圆形的
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //画点，位置加画笔
        canvas.drawPoint(800, 800, mPaint);
        mPaint.setColor(Color.YELLOW);
        //连画多个点，数组里两位数一个xy的位置，12位数就是6个点
        canvas.drawPoints(new float[]{100, 100, 200, 200, 300, 300, 400, 400, 500, 500, 600, 600}, mPaint);
        mPaint.setColor(Color.BLUE);
        //后面两个参数，是从数组里的第2位(0是第一位)开始，取6位数，组成的数组画出来，相当于canvas.drawPoints(new float[]{100, 200, 200, 300, 300, 400}, mPaint);
        canvas.drawPoints(new float[]{100, 100, 200, 200, 300, 300, 400, 400, 500, 500, 600, 600}, 1, 6, mPaint);

        mPaint.reset();
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.RED);
        canvas.drawLine(100, 500, 500, 500, mPaint);
        mPaint.setColor(Color.YELLOW);
        //12个数组3条线
        canvas.drawLines(new float[]{100, 600, 500, 600, 100, 700, 500, 700, 100, 800, 500, 800}, mPaint);
        mPaint.setColor(Color.BLUE);
        //第offset位取count位数，4位一组画条线，多出来的不够4位的不管
        canvas.drawLines(new float[]{100, 900, 500, 900, 100, 1000, 500, 1000, 100, 1100, 500, 1100}, 1, 9, mPaint);

        mPaint.setColor(Color.RED);
        //确定上下左右位置画矩形
        canvas.drawRect(30, 30, 180, 150, mPaint);
        mPaint.setColor(Color.YELLOW);
        //通过矩形对象(Rect)画矩形
        Rect rect = new Rect(30, 180, 180, 300);
        //x方向偏移20y方向不动
        rect.offset(20, 0);
        canvas.drawRect(rect, mPaint);

        mPaint.setColor(Color.RED);
        //通过上下左右画椭圆，需要21版本以上
        canvas.drawOval(30, 330, 180, 450, mPaint);
        mPaint.setColor(Color.YELLOW);
        //通过矩形画椭圆
        canvas.drawOval(new RectF(30, 480, 180, 600), mPaint);

        mPaint.setColor(Color.RED);
        //画圆，前两个是原点位置，第三个是半径
        canvas.drawCircle(500, 100, 150, mPaint);

        mPaint.setColor(Color.RED);
        //前面4位先确定一个椭圆，0度是中心往右的直线，往下为正，上是负，下面是从0°画到270°，第6个参数是填充
        canvas.drawArc(330, 330, 500, 600, 0, 270, true, mPaint);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStyle(Paint.Style.STROKE);
        //跟上面一样，从0°画到90°，刚好是右下角四分之一个椭圆，360°就是完整椭圆，第6个参数不填充就是只画外层的线
        canvas.drawArc(new RectF(330, 1000, 500, 1300), 0, 90, false, mPaint);

        mPaint.setColor(Color.RED);
        //画圆角矩形，前4位先画矩形，5、6位是x,y方向的圆角
        canvas.drawRoundRect(30, 630, 180, 800, 16, 16, mPaint);
        mPaint.setColor(Color.YELLOW);
        //圆角矩形，给一个矩形，第2、3位是x\y的圆角，x表示4个角都是x方向偏进去20，y都偏进去50
        canvas.drawRoundRect(new RectF(30, 900, 180, 1070), 20, 50, mPaint);

        //画路径
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        //路径类
        Path path = new Path();
        //移动到一个点
        path.moveTo(50, 300);
        //画到一个点
        path.lineTo(50, 500);
        path.lineTo(100, 700);
        path.lineTo(400, 700);
        path.lineTo(300, 500);
        path.lineTo(450, 250);
        //移动到一个点
        path.moveTo(200, 400);
        path.lineTo(50,300);
        canvas.drawPath(path, mPaint);
        mPaint.reset();
        mPaint.setColor(Color.BLACK);
        //设置对齐方式，右对齐就是设置的基准点往左偏，一般左对齐
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(60);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //先画条线做基准
        canvas.drawLine(0, 800, 1400, 800, mPaint);
        //从x0，y10开始写(不是指最底线，是下面很接近底线的基准线)，对应字的左下角，也就是只有10个高留给字，不够的都跑到上面去了
        canvas.drawText("adslfkjsdlfj", 0, 800, mPaint);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.RED);
        p.setStrokeWidth(10);
        PathEffect effects = new DashPathEffect(new float[] { 10, 20, 40, 80}, 1);
        p.setPathEffect(effects);
        canvas.drawLine(0, 840, 500, 840, p);
    }
}
