package com.hitwh.apptemplate.common.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BitmapUtil {
    private static final Size ZERO_SIZE = new Size(0, 0);
    private static final BitmapFactory.Options OPTIONS_GET_SIZE = new BitmapFactory.Options();
    private static final BitmapFactory.Options OPTIONS_DECODE = new BitmapFactory.Options();
    private static final byte[] LOCKED = new byte[0];
    // 此对象用来保持Bitmap的回收顺序,保证最后使用的图片被回收
    private static final LinkedList CACHE_ENTRIES = new LinkedList();
    // 线程请求创建图片的队列
    private static final Queue TASK_QUEUE = new LinkedList();
    // 保存队列中正在处理的图片的key,有效防止重复添加到请求创建队列
    private static final Set TASK_QUEUE_INDEX = new HashSet();
    // 缓存Bitmap
    private static final Map IMG_CACHE_INDEX = new HashMap();// 通过图片路径,图片大小
    private static int CACHE_SIZE = 20; // 缓存图片数量

    static {
        OPTIONS_GET_SIZE.inJustDecodeBounds = true;
        // 初始化创建图片线程,并等待处理
        new Thread() {
            {
                setDaemon(true);
            }

            public void run() {
                while (true) {
                    synchronized (TASK_QUEUE) {
                        if (TASK_QUEUE.isEmpty()) {
                            try {
                                TASK_QUEUE.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    QueueEntry entry = (QueueEntry) TASK_QUEUE.poll();
                    String key = createKey(entry.path, entry.width,
                            entry.height);
                    TASK_QUEUE_INDEX.remove(key);
                    createBitmap(entry.path, entry.width, entry.height);
                }
            }
        }.start();
    }


    public static Bitmap getBitmap(String path, int width, int height) {
        if (path == null) {
            return null;
        }
        Bitmap bitMap = null;
        try {
            if (CACHE_ENTRIES.size() >= CACHE_SIZE) {
                destoryLast();
            }
            bitMap = useBitmap(path, width, height);
            if (bitMap != null && !bitMap.isRecycled()) {
                return bitMap;
            }
            bitMap = createBitmap(path, width, height);
            String key = createKey(path, width, height);
            synchronized (LOCKED) {
                IMG_CACHE_INDEX.put(key, bitMap);
                CACHE_ENTRIES.addFirst(key);
            }
        } catch (OutOfMemoryError err) {
            destoryLast();
            System.out.println(CACHE_SIZE);
            return createBitmap(path, width, height);
        }
        return bitMap;
    }

    /**
     * Get bitmap from specified image path
     *
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }


    public static Size getBitMapSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                BitmapFactory.decodeStream(in, null, OPTIONS_GET_SIZE);
                return new Size(OPTIONS_GET_SIZE.outWidth,
                        OPTIONS_GET_SIZE.outHeight);
            } catch (FileNotFoundException e) {
                return ZERO_SIZE;
            } finally {
                closeInputStream(in);
            }
        }
        return ZERO_SIZE;
    }

    // ------------------------------------------------------------------ private Methods
    // 将图片加入队列头
    private static Bitmap useBitmap(String path, int width, int height) {
        Bitmap bitMap = null;
        String key = createKey(path, width, height);
        synchronized (LOCKED) {
            bitMap = (Bitmap) IMG_CACHE_INDEX.get(key);
            if (null != bitMap) {
                if (CACHE_ENTRIES.remove(key)) {
                    CACHE_ENTRIES.addFirst(key);
                }
            }
        }
        return bitMap;
    }

    // 回收最后一张图片
    private static void destoryLast() {
        synchronized (LOCKED) {
            String key = (String) CACHE_ENTRIES.removeLast();
            if (key.length() > 0) {
                Bitmap bitMap = (Bitmap) IMG_CACHE_INDEX.remove(key);
                if (bitMap != null && !bitMap.isRecycled()) {
                    bitMap.recycle();
                    bitMap = null;
                }
            }
        }
    }

    // 创建键
    private static String createKey(String path, int width, int height) {
        if (null == path || path.length() == 0) {
            return "";
        }
        return path + "_" + width + "_" + height;
    }

    // 通过图片路径,宽度高度创建一个Bitmap对象
    private static Bitmap createBitmap(String path, int width, int height) {
        File file = new File(path);
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                Size size = getBitMapSize(path);
                if (size.equals(ZERO_SIZE)) {
                    return null;
                }
                int scale = 1;
                int a = size.getWidth() / width;
                int b = size.getHeight() / height;
                scale = Math.max(a, b);
                synchronized (OPTIONS_DECODE) {
                    OPTIONS_DECODE.inSampleSize = scale;
                    Bitmap bitMap = BitmapFactory.decodeStream(in, null,
                            OPTIONS_DECODE);
                    return bitMap;
                }
            } catch (FileNotFoundException e) {
                Log.v("BitMapUtil", "createBitmap==" + e.toString());
            } finally {
                closeInputStream(in);
            }
        }
        return null;
    }

    // 关闭输入流
    private static void closeInputStream(InputStream in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                Log.v("BitMapUtil", "closeInputStream==" + e.toString());
            }
        }
    }

    // 图片大小
    static class Size {
        private int width, height;

        Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    // 队列缓存参数对象
    static class QueueEntry {
        public String path;
        public int width;
        public int height;
    }

    public static void saveBitmap(Bitmap mBitmap, String bitName) {
        File f = new File(FileUtil.ALBUM_PATH_2 + bitName + ".jpg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(File file) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 剪切照片到"License"目录
     *
     * @param croppedFileUri 剪切照片的URI
     * @throws Exception 异常
     */

    public static void moveFileToCut(Uri croppedFileUri) throws Exception {

        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(),

                croppedFileUri.getLastPathSegment());


        File saveFile = new File(CommonUtils.createDirs(CommonUtils.LICENSE_PATH), filename);


        FileInputStream instream = new FileInputStream(new File(croppedFileUri.getPath()));

        FileOutputStream outStream = new FileOutputStream(saveFile);

        FileChannel inChannel = instream.getChannel();

        FileChannel outChannel = outStream.getChannel();

        inChannel.transferTo(0, inChannel.size(), outChannel);

        instream.close();

        outStream.close();

//        licensePhotoView.notifyImageChanged(saveFile.getPath());

//        CommonUtils.deleteFile(Const.uri.getPath());

    }


    /**
     * 将Bitmap转换成base64字符串
     *
     * @param bitmap        图片
     * @param bitmapQuality 图片质量， 100为不压缩
     * @return base64编码
     */
    public static String bitmapToString(Bitmap bitmap, int bitmapQuality) {
        String result = "";
        ByteArrayOutputStream baos = null;
        try {
            if (null != bitmap) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, bitmapQuality, baos);//将bitmap放入字节数组流中

                baos.flush();//将bos流缓存在内存中的数据全部输出，清空缓存
                baos.close();

                byte[] bitmapByte = baos.toByteArray();
                result = Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != baos) {
                try {
                    baos.flush();
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
