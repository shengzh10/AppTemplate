package com.hitwh.apptemplate.common.javaBean;

import java.util.ArrayList;


public class BaseBean<T> {

    private StatusBean status;

    private ArrayList<T> dataList;

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }
}
