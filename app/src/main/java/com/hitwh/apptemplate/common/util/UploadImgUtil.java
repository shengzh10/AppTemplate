package com.hitwh.apptemplate.common.util;

import java.util.List;

/**
 * @author SQY
 * @since 2017/4/1.
 */

public class UploadImgUtil {

    private List<String> queueImgPathList; // 待上传的图片地址列表

    private List<String> defeatImgPathList; // 上传失败的图片地址列表

    public static UploadImgUtil newInstance(List<String> imgPathList) {
        UploadImgUtil fragment = new UploadImgUtil(imgPathList);
        return fragment;
    }

    public UploadImgUtil(List<String> queueImgPathList) {
        this.queueImgPathList = queueImgPathList;
    }

    public UploadImgUtil() {
    }

    /**
     * 上传图片
     */
    public void upload(){

    }
}
