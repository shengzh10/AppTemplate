package com.hitwh.apptemplate.common.util;

import android.os.Environment;
import android.util.Log;

import com.hitwh.apptemplate.common.javaBean.FileBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FileUtil {

    private static String SecondFolderImage = "image";//一级目录
    private static String FirstFolder = "PdPatrol";//一级目录
    private static String SecondFolder = "file";//一级目录

    public static String ALBUM_PATH_1 = Environment.getExternalStorageDirectory() + File.separator + FirstFolder + File.separator;

    public static String ALBUM_PATH_2 = Environment.getExternalStorageDirectory() + File.separator + FirstFolder + File.separator + SecondFolder + File.separator;


    /**
     * 创建一级目录
     */
    public static void createFirstDir() {
        File dirFirstFile = new File(ALBUM_PATH_1);//新建一级主目录
        if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
            dirFirstFile.mkdir();//如果不存在则创建
        }
        File dirSecondFile = new File(ALBUM_PATH_2);//新建二级主目录
        if (!dirSecondFile.exists()) {//判断文件夹目录是否存在
            dirSecondFile.mkdir();//如果不存在则创建
        }
    }


    /**
     * 获取全部文件分类
     *
     * @return
     */
    public static ArrayList<String> getFileCategory() {
        //先创建一级目录
        createFirstDir();
        ArrayList<String> fileCategory = new ArrayList<>();
        // 得到该路径文件夹下所有的文件
        File fileAll = new File(ALBUM_PATH_2);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中
        for (File file : files) {
            fileCategory.add(file.getName());
            Log.d("FileUtil", file.getName());
        }
        return fileCategory;
    }


    /**
     * 获取全部下载文件
     */
    public static ArrayList<FileBean> getAllDownloadFiles(ArrayList<String> categories) {
        ArrayList<FileBean> fileList = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            String path = FileUtil.ALBUM_PATH_2 + File.separator + categories.get(i) + File.separator;
            fileList.addAll(getFiles(path));
        }
        return fileList;
    }

    /**
     * 获取具体分类文件夹下的所有文件
     */
    public static ArrayList<FileBean> getFiles(String path) {

        ArrayList<FileBean> fileList = new ArrayList<>();

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(path);
        File[] files = fileAll.listFiles();
        // 将所有的文件存入ArrayList中
        for (File file : files) {
            FileBean fileBean = new FileBean();
            //文件全名称 文件编号(数字)_文件名(任意字符中文+英文+数字).扩展名(英文)
            String fileName = file.getName();
            //文件路径
            fileBean.setFilepath(file.getPath());
            Log.d("FileUtil", "path: " + fileBean.getFilepath());
            //文件名称
            fileBean.setFileName(fileName.substring(fileName.indexOf("_")).substring(1));
            Log.d("FileUtil", "name: " + fileBean.getFileName());
            //文件编号
            fileBean.setFileCode(fileName.substring(0, fileName.indexOf("_")));
            Log.d("FileUtil", "code: " + fileBean.getFileCode());

            fileList.add(fileBean);
        }
        return fileList;
    }

    /**
     * 删除文件或者文件夹
     */
    public static boolean deleteFile(String path) {
        if (isBlank(path)) {
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

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }


    /**
     * 创建分类目录
     */
    public static void createCategoryDir(String category) {
        File dirFirstFile = new File(ALBUM_PATH_1);//新建一级主目录
        if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
            dirFirstFile.mkdir();//如果不存在则创建
        }
        File dirSecondFile = new File(ALBUM_PATH_2);//新建二级主目录
        if (!dirSecondFile.exists()) {//判断文件夹目录是否存在
            dirSecondFile.mkdir();//如果不存在则创建
        }
        File dirThirdFile = new File(ALBUM_PATH_2 + category + File.separator);
        if (!dirThirdFile.exists()) {//判断文件夹目录是否存在
            dirThirdFile.mkdir();//如果不存在则创建
        }
    }

    public static File createFile(String category, String fileName) {
        createCategoryDir(category);
        Log.d("FileUtil", "PATH:" + ALBUM_PATH_2 + category + File.separator);
        return new File(ALBUM_PATH_2 + category + File.separator + fileName);
    }

    /**
     * 将文件写入SD卡
     *
     * @param response
     * @param file
     * @return
     */
    public static boolean writeFile2Disk(Response<ResponseBody> response, File file) {
        long currentLength = 0;
        OutputStream os = null;

//        InputStream is2 = null;
//        OutputStream os2 = null;
//        HWPFDocument doc = null;
        //服务器返回的文件response转换为字节流
        InputStream is = response.body().byteStream();
        long totalLength = response.body().contentLength();
        try {
            os = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;

                }
            }

        }


        //是否下载完成全部的内容
        if (totalLength == currentLength) {
            //下载成功
            return true;
        } else {
            //下载失败
            return false;
        }
    }


    public static boolean writeResponseBodyToDisk(ResponseBody body, File file) {
        try {
            File futureStudioIconFile = file;

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;

            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建存放图片的目录
     */
    public static File createDirs(String taskNumber) {
        //一级目录和二级目录必须分开创建
        File dirFirstFile = new File(ALBUM_PATH_1);//新建一级主目录
        if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
            dirFirstFile.mkdir();//如果不存在则创建
        }
        File dirSecondFile = new File(ALBUM_PATH_1 + SecondFolderImage + taskNumber + File.separator);//新建二级主目录
        if (!dirSecondFile.exists()) {//判断文件夹目录是否存在
            dirSecondFile.mkdir();//如果不存在则创建
        }
        return dirSecondFile;
    }

    public static String createPhotoFileName() {
        String fileName = "";
        Date date = new Date(System.currentTimeMillis());  //系统当前时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        fileName = dateFormat.format(date) + ".jpg";
        return fileName;
    }

}
