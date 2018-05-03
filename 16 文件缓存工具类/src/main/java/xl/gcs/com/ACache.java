package xl.gcs.com;

/**
 * 小陪APP  缓存
 * Created by mcj on 2017/1/10.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static android.content.ContentValues.TAG;

public class ACache {
    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb,最多不超过50mb数据
    private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
    private static Map<String, ACache> mInstanceMap = new HashMap<String, ACache>();
    private ACacheManager mCache;

    //如果不设缓存文件名的话就默认为ACache
    public static ACache get(Context ctx) {
        return get(ctx, "ACache");
    }

    //如果有传文件名就建好文件，附带默认数据限制大小，限制数量，再传给重构方法
    public static ACache get(Context ctx, String cacheName) {
        //是这个目录，不用权限/data/user/0/xl.gcs.com/files/ACache
        File f = new File(ctx.getFilesDir(), cacheName);
        Log.d(TAG, "get: " + f.getAbsolutePath());
        return get(f, MAX_SIZE, MAX_COUNT);
    }

    public static ACache get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    //如果自己设定了限制数量与容量，就一起传过去
    public static ACache get(Context ctx, long max_zise, int max_count) {
        File f = new File(ctx.getFilesDir(), "ACache");
        return get(f, max_zise, max_count);
    }

    //真正的返回实例方法
    public static ACache get(File cacheDir, long max_zise, int max_count) {
        //这里全局变量读取，只要进程存在就会一直可以读取，进程被回收了就要重新创建了
        ACache manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (manager == null) {
            manager = new ACache(cacheDir, max_zise, max_count);
            //把对象放入全局变量的hashMap，键就是文件名+线程名
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
        }
        return manager;
    }

    private static String myPid() {
        //android.os.Process.myPid()是返回一个进程号，只要进程不退出就不变，退出后重启程序就会有新的进程号
        return "_" + android.os.Process.myPid();
    }


    private ACache(File cacheDir, long max_size, int max_count) {
        //如果文件不存在同时不能创建就返回错误，正常已经创建了文件了
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in "
                    + cacheDir.getAbsolutePath());
        }
        //里面会计算总大小，文件总数量，文件为键，最后修改时间为值的Map,还有传入最大文件数量，和最大容量，还有目录地址
        mCache = new ACacheManager(cacheDir, max_size, max_count);
    }

    // =======================================
    // ============ String数据 读写 ==============
    // =======================================

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {
        //file = 在ACache里再加一个文件，文件名是key的hashCode,此时ACache已经创建了，文件还没有，只是有个路径，一会会通过FileWriter创建简单文件，准备存入要存的value
        File file = mCache.newFile(key);
        BufferedWriter out = null;
        try {
            //FileWriter用于写入字符文件的便捷类。此类的构造方法假定默认字符编码和默认字节缓冲区大小都是可接受的。这样就创建了以key的hashCode为文件名，放在ACache的文件
            out = new BufferedWriter(new FileWriter(file), 1024);
            //写入数据，最后会是/storage/emulated/0/Android/data/xl.gcs.com/files/ACache/99300的路径，99300是没有后缀的文件，可以通过文本类的程序打开，读取value
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    //类似缓冲写入
                    out.flush();
                    //关闭
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }

    /**
     * 保存 String数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的String数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, String value, int saveTime) {
        //第一位参数是储存的键，下次通过key来读取
        //第二位参数是，通过 Utils.newStringWithDateInfo返回现在的时间（至少13位）+ "-" + 传入的时间，单位是秒 + 空格 + 需要保存的字符串
        put(key, Utils.newStringWithDateInfo(saveTime, value));
    }

    /**
     * 读取 String数据
     *
     * @param key
     * @return String 数据
     */
    public String getAsString(String key) {
        //这里会先创建一个文件，再把原来的文件给替换掉，数据还是原来的数据，修改时间变了
        File file = mCache.get(key);
        //如果这文件还没创建，说明还没有存过这个key，没有文件也不可能有数据
        if (!file.exists()) {
            //就返回null，这里其实不用删也没事，主要是Map里数据多了条空的，总文件数量没有+1的
            return null;
        }
        boolean removeFile = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            //如果没到期
            if (!Utils.isDue(readString)) {
                //就会得到value的内容
                return Utils.clearDateInfo(readString);
            } else {
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //到期了就删掉
            if (removeFile)
                remove(key);
        }
    }

    // =======================================
    // ============= JSONObject 数据 读写 ==============
    // =======================================

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSON数据
     */
    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的JSONObject数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONObject value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONObject数据
     *
     * @param key
     * @return JSONObject数据
     */
    public JSONObject getAsJSONObject(String key) {
        String JSONString = getAsString(key);
        try {
            JSONObject obj = new JSONObject(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============ JSONArray 数据 读写 =============
    // =======================================

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSONArray数据
     */
    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的JSONArray数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, JSONArray value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * 读取JSONArray数据
     *
     * @param key
     * @return JSONArray数据
     */
    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj = new JSONArray(JSONString);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // =======================================
    // ============== byte 数据 读写 =============
    // =======================================

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        File file = mCache.newFile(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (Exception e) {
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
            mCache.put(file);
        }
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, byte[] value, int saveTime) {
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }

    /**
     * 获取 byte 数据
     *
     * @param key
     * @return byte 数据
     */
    public byte[] getAsBinary(String key) {
        //RandomAccessFile是用来访问那些保存数据记录的文件的，你就可以用seek( )方法在某一段开始插入数据，可以某一段开始读取数据，这里为了避开前面的格式，直接从value开始读取
        RandomAccessFile RAFile = null;
        boolean removeFile = false;
        try {
            File file = mCache.get(key);
            if (!file.exists())
                return null;
            //r表示文件只读
            RAFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) RAFile.length()];
            RAFile.read(byteArray);
            //判断如果文件格式正确，并且没超时
            if (!Utils.isDue(byteArray)) {
                //这里会读取value，如果格式错误的话，会原封不动返回byteArray
                return Utils.clearDateInfo(byteArray);
            } else {
                //如果超时了，就标记下
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (RAFile != null) {
                try {
                    RAFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //如果超时了，就会删除数据
            if (removeFile)
                remove(key);
        }
    }

    // =======================================
    // ============= 序列化 数据 读写 ===============
    // =======================================

    /**
     * 保存 Serializable数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * 保存 Serializable数据到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的value
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 读取 Serializable数据
     *
     * @param key
     * @return Serializable 数据
     */
    public Object getAsObject(String key) {
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                Object reObject = ois.readObject();
                return reObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null)
                        bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null)
                        ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    // =======================================
    // ============== bitmap 数据 读写 =============
    // =======================================

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的bitmap数据
     */
    public void put(String key, Bitmap value) {
        put(key, Utils.Bitmap2Bytes(value));
    }

    /**
     * 保存 bitmap 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的 bitmap 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Bitmap value, int saveTime) {
        put(key, Utils.Bitmap2Bytes(value), saveTime);
    }

    /**
     * 读取 bitmap 数据
     *
     * @param key
     * @return bitmap 数据
     */
    public Bitmap getAsBitmap(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return Utils.Bytes2Bimap(getAsBinary(key));
    }

    // =======================================
    // ============= drawable 数据 读写 =============
    // =======================================

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的drawable数据
     */
    public void put(String key, Drawable value) {
        put(key, Utils.drawable2Bitmap(value));
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的 drawable 数据
     * @param saveTime 保存的时间，单位：秒
     */
    public void put(String key, Drawable value, int saveTime) {
        put(key, Utils.drawable2Bitmap(value), saveTime);
    }

    /**
     * 读取 Drawable 数据
     *
     * @param key
     * @return Drawable 数据
     */
    public Drawable getAsDrawable(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return Utils.bitmap2Drawable(Utils.Bytes2Bimap(getAsBinary(key)));
    }

    /**
     * 获取缓存文件
     *
     * @param key
     * @return value 缓存的文件
     */
    public File file(String key) {
        File f = mCache.newFile(key);
        if (f.exists())
            return f;
        return null;
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public boolean remove(String key) {
        return mCache.remove(key);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        mCache.clear();
    }

    /**
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     * @title 缓存管理器
     */
    public class ACacheManager {
        //AtomicLong是作用是对长整形进行原子操作,这个和下面这个可以理解为安全线程的类。java1.8后有更好的类LongAdder
        private final AtomicLong cacheSize;
        //是一个提供原子操作的Integer类，通过线程安全的方式操作加减。
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final int countLimit;
        //它对map中的所有桶加了锁。所以，只要要有一个线程访问map，其他线程就无法进入map,我们不能对这些同步的map过于信任，而忽略了混合操作带来的影响。正确的方法是，把map的读取和清空看成一个原子操作，给整个代码块加锁
        private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
        protected File cacheDir;

        //文件目录，最大文件容量，最大文件数量
        private ACacheManager(File cacheDir, long sizeLimit, int countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new AtomicLong();
            cacheCount = new AtomicInteger();
            //这方法后就得到了cacheSize（总容量）,cacheCount(总数量),lastUsageDates(以文件为键，最后修改时间为值)
            calculateCacheSizeAndCacheCount();
        }

        /**
         * 开启线程计算 总文件数和总容量，并存入lastUsageDates
         */
        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    //把这个目录里的文件通过数组返回
                    File[] cachedFiles = cacheDir.listFiles();
                    //如果有文件
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            //作者白痴方法，其实就是file.length()
                            size += calculateSize(cachedFile);
                            count += 1;
                            //把文件当做键，值是最后修改时间cachedFile.lastModified()，存进去
                            lastUsageDates.put(cachedFile,
                                    cachedFile.lastModified());
                        }
                        //存入总数量和总容量
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }
                }
            }).start();
        }

        private void put(File file) {
            //先查一查文件的总数量
            int curCacheCount = cacheCount.get();
            //只要再加个文件就超过了限制的总数量
            while (curCacheCount + 1 > countLimit) {
                //removeNext()会删除掉存最久的文件，并且读取文件大小，下面就存入freedSize
                long freedSize = removeNext();
                //下面就把全部文件大小加个已删除文件大小的负数，就是减掉
                cacheSize.addAndGet(-freedSize);
                //文件总数-1
                curCacheCount = cacheCount.addAndGet(-1);
            }
            //再文件总数+1，因为要存入新数据了
            cacheCount.addAndGet(1);
            //作者白痴方法，其实就是file.length()，先记录这文件大小，和刚刚删完后，剩下文件还有多大
            long valueSize = calculateSize(file);
            long curCacheSize = cacheSize.get();
            //只要剩下的总文件大小，加上这条，还是超出了sizeLimit
            while (curCacheSize + valueSize > sizeLimit) {
                //就继续删掉最老的
                long freedSize = removeNext();
                //然后再减掉删掉文件的大小，记录下现在的大小，再继续比对
                curCacheSize = cacheSize.addAndGet(-freedSize);
                //这条我私加的，作者应该漏掉这条了，这里运行多次，应该每次都要减去总文件数，不然之后就不知道删了几条，不可能做到一次性减
                cacheCount.addAndGet(-1);
            }
            //然后加上这个文件，因为刚刚已经判断了，现在加上也不可能超出sizeLimit
            cacheSize.addAndGet(valueSize);
            //记录下现在的时间
            Long currentTime = System.currentTimeMillis();
            //设置最后的修改时间
            file.setLastModified(currentTime);
            //存入Map集合
            lastUsageDates.put(file, currentTime);
        }

        //读取文件方法
        private File get(String key) {
            //同样要先获取hashCode
            File file = newFile(key);
            //先取得现在的时间，设置该文件的最后修改时间
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            //存入文件和最后修改时间，会把之前那个文件给挤掉，主要是为了把时间设为最新
            lastUsageDates.put(file, currentTime);
            return file;
        }

        private File newFile(String key) {
            //返回这目录加key的哈希码
            return new File(cacheDir, key.hashCode() + "");
        }

        private boolean remove(String key) {
            File image = get(key);
            return image.delete();
        }

        private void clear() {
            lastUsageDates.clear();
            cacheSize.set(0);
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        /**
         * 移除旧的文件
         *
         * @return
         */
        private long removeNext() {
            //如果存文件的Map类是空的，就不用往下了，没什么好删的
            if (lastUsageDates.isEmpty()) {
                return 0;
            }
            Long oldestUsage = null;
            File mostLongUsedFile = null;
            //注重独一无二的性质,该体系集合可以知道某物是否已近存在于集合中,不会存储重复的元素
            //Map提供了一些常用方法，如keySet()、entrySet()等方法，keySet()方法返回值是Map中key值的集合；entrySet()的返回值也是返回一个Set集合，此集合的类型为Map.Entry
            Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
            synchronized (lastUsageDates) {
                //遍历lastUsageDates里的每个值，按照这轮遍历，会把最久的修改时间选出来，并且读取到File给mostLongUsedFile
                for (Entry<File, Long> entry : entries) {
                    if (mostLongUsedFile == null) {
                        //第一次先取得键（就是文件），和最后的修改时间
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                    } else {
                        //如果已经有第一个文件了，就再获取下一个文件的修改时间
                        Long lastValueUsage = entry.getValue();
                        //如果这个修改时间比之前那个小（说明保存时间更久，最后会把最久的删掉）
                        if (lastValueUsage < oldestUsage) {
                            //把这个放着继续做比对，相当于，刚刚那个在这轮删除里会安全，下面同时留着修改时间和文件
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = entry.getKey();
                        }
                    }
                }
            }
            //笨方法，就是记录刚那个mostLongUsedFile.length
            long fileSize = calculateSize(mostLongUsedFile);
            //删除文件，如果删除成功
            if (mostLongUsedFile.delete()) {
                //就再map里移除这条
                lastUsageDates.remove(mostLongUsedFile);
            }
            //返回文件大小
            return fileSize;
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }

    /**
     * @author 杨福海（michael） www.yangfuhai.com
     * @version 1.0
     * @title 时间计算工具类
     */
    private static class Utils {

        /**
         * 判断缓存的String数据是否到期
         *
         * @param str
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        /**
         * 判断缓存的byte数据是否到期
         *
         * @param data
         * @return true：到期了 false：还没有到期
         */
        private static boolean isDue(byte[] data) {
            //这里的数组是两位数，第一位是储存日期，第二位是储存时间
            String[] strs = getDateInfoFromDate(data);
            if (strs != null && strs.length == 2) {
                String saveTimeStr = strs[0];
                //String.startsWith表示是否从某个前缀开始，这里是看看第一位是不是0
                while (saveTimeStr.startsWith("0")) {
                    //如果是0，就把0去掉
                    saveTimeStr = saveTimeStr
                            .substring(1, saveTimeStr.length());
                }
                //两个都转成long
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strs[1]);
                //现在时间，如果大于储存的开始时间+储存的有效时间，就说明超时了
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                    return true;
                }
            }
            return false;
        }

        //private static 方便给本外部类用的静态方法（不用new）
        private static String newStringWithDateInfo(int second, String strInfo) {
            //返回现在的时间（至少13位）+ "-" + 传入的时间，单位是秒 + 空格 + 需要保存的字符串
            return createDateInfo(second) + strInfo;
        }

        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
            byte[] data1 = createDateInfo(second).getBytes();
            byte[] retdata = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retdata, 0, data1.length);
            System.arraycopy(data2, 0, retdata, data1.length, data2.length);
            return retdata;
        }

        private static String clearDateInfo(String strInfo) {
            if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
                strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1,
                        strInfo.length());
            }
            return strInfo;
        }

        private static byte[] clearDateInfo(byte[] data) {
            //再次检查格式是否正确
            if (hasDateInfo(data)) {
                //返回从空格到全部，刚好就是value
                return copyOfRange(data, indexOf(data, mSeparator) + 1,
                        data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            //数据不等于null,大小必须大于15(日期long就补足13位了，还有-，还有空格，还有传入时间的秒数，肯定会超过15位),13位日期后必须跟-，刚刚就是这么存的
            //空格是在最后，这么多数据加起来肯定是大于14的，所以，只要按照格式存的，肯定返回true
            return data != null && data.length > 15 && data[13] == '-'
                    && indexOf(data, mSeparator) > 14;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            //先验证下格式对不对
            if (hasDateInfo(data)) {
                //得到日期的long，按照String读出，这里是储存日期
                String saveDate = new String(copyOfRange(data, 0, 13));
                //这里是知道准备存多久
                String deleteAfter = new String(copyOfRange(data, 14,
                        indexOf(data, mSeparator)));
                //返回数组
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        //看下字符的第几位等于 c,我们会传入空格比对，看空格在第几位
        private static int indexOf(byte[] data, char c) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0)
                throw new IllegalArgumentException(from + " > " + to);
            byte[] copy = new byte[newLength];
            //专门复制一个字节数组，显示日期数
            System.arraycopy(original, from, copy, 0,
                    Math.min(original.length - from, newLength));
            return copy;
        }

        private static final char mSeparator = ' ';

        private static String createDateInfo(int second) {
            //现在的时间long数值 转String
            String currentTime = System.currentTimeMillis() + "";
            //如果不够13位，就加0
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime;
            }
            //返回现在的时间（至少13位）+ "-" + 传入的时间，单位是秒 + 空格
            return currentTime + "-" + second + mSeparator;
        }

        /*
         * Bitmap → byte[]
         */
        private static byte[] Bitmap2Bytes(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }

        /*
         * byte[] → Bitmap
         */
        private static Bitmap Bytes2Bimap(byte[] b) {
            if (b.length == 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        /*
         * Drawable → Bitmap
         */
        private static Bitmap drawable2Bitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            return bitmap;
        }

        /*
         * Bitmap → Drawable
         */
        @SuppressWarnings("deprecation")
        private static Drawable bitmap2Drawable(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            return new BitmapDrawable(bm);
        }
    }

}


