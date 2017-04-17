package com.hitwh.apptemplate.common.util;

/**
 * @author charleyFeng
 * @since 16/11/29.
 */

public class StringUtil {
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 根据图片路径获取图片名称
     *
     * @param photoPath 图片路径
     * @return
     */
    public static String getPhotoName(String photoPath) {
        return photoPath.substring(photoPath.lastIndexOf("/"), photoPath.lastIndexOf("."));
    }
}
