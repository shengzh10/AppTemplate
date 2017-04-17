package com.hitwh.apptemplate.common.javaBean;


public class FileBean {

    private String fileName;//文件名.doc
    private String fileCode;//文件全名：文件名_文件编号.doc
    private String filepath;//文件本地路径
    private String downloadDate;//文件时间

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }
}
