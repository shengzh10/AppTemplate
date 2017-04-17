package com.hitwh.apptemplate.common.base;

import rx.Observable;
import rx.Subscriber;

/**
 * @author SQY
 * @since 2017/3/15.
 */

public interface BasePresenter<V extends BaseView> {
    /**
     * 绑定view
     *
     * @param view 显示的view
     */
    void attachView(V view);

    /**
     * 解绑view
     */
    void detachView();

    /**
     * 添加从server获取数据的RxJava注册
     *
     * @param observable 被观察者
     * @param subscriber 观察者
     */
    void addServerSubscription(Observable observable, Subscriber subscriber);

    /**
     * 添加从数据库获取数据的RxJava注册
     *
     * @param observable 被观察者
     * @param subscriber 观察者
     */
    void addDBSubscription(Observable observable, Subscriber subscriber);

    /**
     * RxJava取消注册
     */
    void onUnsubscribe();

    /**
     * 返回
     */
    void returnBack();
}
