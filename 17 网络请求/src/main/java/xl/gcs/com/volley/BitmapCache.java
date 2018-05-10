package xl.gcs.com.volley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2016/4/3.
 */
    public class BitmapCache implements ImageLoader.ImageCache {

        //LruCache是android提供的一个缓存工具类，其算法是最近最少使用算法。它把最近使用的对象用“强引用”存储在LinkedHashMap中，并且把最近最少使用的对象在缓存值达到预设定值之前就从内存中移除。
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 8 * 1024 * 1024;
            //android的缓存类
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                //Sizeof方法的作用只要是定义缓存中每项的大小，当我们缓存进去一个数据后，当前已缓存的Size就会根据这个方法将当前加进来的数据也加上，便于统计当前使用了多少内存，如果已使用的大小超过maxSize就会进行清除动作；
                protected int sizeOf(String key, Bitmap bitmap) {
                    //getRowBytes()：每一行所占的空间数。getByteCount()：BitMap的大小。为什么在一般情况下不用bitmap.getByteCount()呢？因为getByteCount要求的API版本较高，考虑到兼容性，一般使用上面的getRowBytes方法。
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap)     {
            mCache.put(url, bitmap);
        }

}
