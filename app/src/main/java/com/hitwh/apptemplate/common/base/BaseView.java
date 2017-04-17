package com.hitwh.apptemplate.common.base;

import com.nispok.snackbar.listeners.ActionClickListener;

/**
 * @author SQY
 * @since 2017/3/15.
 */

public interface BaseView {

    /**
     * 显示 耗时操作
     */
    void showLoading();

    /**
     * 隐藏 耗时操作
     */
    void dismissLoading();

    /**
     * 显示警告
     *
     * @param text 警示内容
     */
    void warning(String text);

    /**
     * 显示提示
     *
     * @param text 提示内容
     */
    void notice(String text);

    /**
     * 自定义操作的提醒
     *
     * @param listener
     */
    void notice(String text, ActionClickListener listener);

    /**
     * 显示进度条
     */
    void showProgressBar(int count);

    /**
     * 更新进度条进度
     */
    void updateProgress(int index, int count);

    /**
     * 隐藏进度条
     */
    void dismissProgressBar();
}
