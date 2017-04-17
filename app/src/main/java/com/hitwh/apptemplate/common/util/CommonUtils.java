package com.hitwh.apptemplate.common.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.util.Base64;

import com.hitwh.apptemplate.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class CommonUtils {


    private static final String TAG = "CommonUtils";
    private static final String ALBUM_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "Pdpatrol" + File.separator; // 主文件夹
    public static final String LICENSE_PATH = ALBUM_PATH + "license" + File.separator;  // 证照文件夹
    public static final String ARCHIVE_PATH = ALBUM_PATH + "archive" + File.separator;  // 档案文件夹

    private CommonUtils() {

    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

    /**
     * 创建图片不同的文件名
     */
    public static String createPhotoFileName() {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis());  //系统当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        fileName = dateFormat.format(date) + ".jpg";
        return fileName;
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

    /**
     * 创建文件夹
     * @param folderName 存储目录名称
     * @return 创建的目录
     */
    public static File createDirs(String folderName) {
        File dirFirstFolder = new File(ALBUM_PATH);//新建一级主目录
        if (!dirFirstFolder.exists()) {//判断文件夹目录是否存在
            dirFirstFolder.mkdir();
        }
        File dirSecondFolder = new File(folderName);
        if (!dirSecondFolder.exists()) {
            dirSecondFolder.mkdir();
        }
        return dirSecondFolder;
    }

    /**
     * 删除文件
     * @param path 文件路径
     * @return 删除文件是否成功
     */
    public static boolean deleteFile(String path) {
        if (null == path || 0 == path.trim().length()) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }



    /**
     * 获取照片路径
     * @return 照片路径
     */
    public static ArrayList<String> getPhotoPathFromSD(String path) {
        ArrayList<String> imagePathList = new ArrayList<>();
        File allFile = new File(path);
        File[] files = allFile.listFiles();
        // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
        for (File file : files) {
            if (checkIsImageFile(file.getPath()) && 0 != (getFileSize(file.getPath()))) {
                imagePathList.add(file.getPath());
            } else {
                if (checkIsImageFile(file.getPath()) && 0 == (getFileSize(file.getPath()))) {
                    //删除为空的照片
                    deleteFile(file.getPath());
                }
            }
        }
        return imagePathList;
    }

    /**
     * 获取路径下所有照片
     * @param photoPath 照片路径
     * @return 照片列表
     */
    public static ArrayList<Bitmap> getPhotoList(List<String> photoPath) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (String path : photoPath) {
            Bitmap bitmap = BitmapUtil.getBitmap(path, 100, 100);
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     */
    private static boolean checkIsImageFile(String fName) {
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        return (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp"));
    }

    /**
     * 获取文件大小
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    private static long getFileSize(String path) {
        if (null == path || 0 == path.trim().length()) {
            return -1;
        }
        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

}
