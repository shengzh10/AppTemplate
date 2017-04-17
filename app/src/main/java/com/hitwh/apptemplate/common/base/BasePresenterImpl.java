package com.hitwh.apptemplate.common.base;

import android.util.Log;

import com.hitwh.apptemplate.common.javaBean.BaseBean;
import com.hitwh.apptemplate.common.javaBean.ResultBean;
import com.hitwh.apptemplate.common.util.Const;
import com.hitwh.apptemplate.common.util.StaticFlags;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author SQY
 * @since 2017/3/15.
 */

public abstract class BasePresenterImpl<V extends BaseView, T> implements BasePresenter<V> {
    protected V view;
    protected T model;
    protected UploadPhotoListener uploadPhotoListener; // 上传图片回掉监听
    private CompositeSubscription mCompositeSubscription;
    protected int count = 1;// 上传图片总数

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
        onUnsubscribe();
    }

    /**
     * RXjava取消注册，以避免内存泄露
     */
    @Override
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void addServerSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io()) // 在IO线程中执行
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程中回掉
                .subscribe(subscriber));
    }

    @Override
    public void addDBSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.computation()) // 在计算线程中执行
                .observeOn(AndroidSchedulers.mainThread()) // 在主线程中回掉
                .subscribe(subscriber));
    }

    /**
     * 图片上传
     *
     * @param code       编号： 任务编号 记录编号等
     * @param photoPaths 图片路径
     * @param observable
     */
    public void uploadPhoto(final String code, final List<String> photoPaths, final Observable observable) {
        addServerSubscription(observable,
                new Subscriber<BaseBean<ResultBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.dismissLoading();
                        Log.e("上传照片失败", e.getMessage());
                        view.warning("上传照片失败");
                        view.dismissProgressBar();
                    }

                    @Override
                    public void onNext(BaseBean<ResultBean> state) {
                        // 文字数据上传成功，开始创建图片数据
                        if (StaticFlags.STATUS_SUCCESS.equals(state.getStatus().getMsg())) {
                            Log.e("图片上传成功", "success");
                            // 成功一个之后删除一个
                            photoPaths.remove(photoPaths.size() - 1);
                            if (photoPaths.size() > 0) {
                                // 继续上传图片
                                if (uploadPhotoListener != null) {
                                    view.updateProgress(count - photoPaths.size(), count);
                                    uploadPhoto(code, photoPaths, uploadPhotoListener.createObservable(code, photoPaths));
                                }
                            } else if (photoPaths.size() == 0) {
                                // 上传成功回调
                                view.updateProgress(count, count);
//                                view.showProgressBar();
                                view.dismissProgressBar();
                                if (uploadPhotoListener != null)
                                    uploadPhotoListener.onSuccess();
//                                view.dismissLoading();
                                view.notice("全部上传成功！");
                                Const.imagePath = new ArrayList<String>();
                            }
                        }
                    }
                });
    }


    /**
     * 上传图片回掉监听
     */
    public interface UploadPhotoListener {
        /**
         * 上传成功
         */
        void onSuccess();

        /**
         * 上传失败
         */
        void onError();

        /**
         * 创建当前上传任务的observable
         */
        Observable createObservable(String code, List<String> photoPaths);
    }
}
