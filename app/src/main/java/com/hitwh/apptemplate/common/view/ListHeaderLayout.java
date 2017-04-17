package com.hitwh.apptemplate.common.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.LoadingLayoutBase;
import com.hitwh.apptemplate.R;

/**
 * 下拉刷新界面
 */

public class ListHeaderLayout extends LoadingLayoutBase {
    private FrameLayout mInnerLayout;
    private TextView mHeaderText;
    private ImageView circleImage;

    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;

    private AnimationDrawable animCircle;

    public ListHeaderLayout(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.list_header_loadinglayout, this);
        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        mHeaderText = (TextView) mInnerLayout.findViewById(R.id.tv_refresh_tip);
        circleImage = (ImageView) mInnerLayout.findViewById(R.id.iv_refresh_circle);

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;

        // Load in labels
        mPullLabel = context.getString(R.string.list_pull_label);
        mRefreshingLabel = context.getString(R.string.list_refresh_label);
        mReleaseLabel = context.getString(R.string.list_release_label);

        reset();
    }

    /**
     * 获取"加载头部"高度
     * @return 高度
     */
    @Override
    public int getContentSize() {
        // 设置未完全显示的时候就促发刷新动作
        return mInnerLayout.getHeight();
    }

    /**
     * 开始下拉时的回调
     */
    @Override
    public void pullToRefresh() {
        mHeaderText.setText(mPullLabel);
    }

    /**
     * "加载头部"完全显示时的回调
     */
    @Override
    public void releaseToRefresh() {
        mHeaderText.setText(mReleaseLabel);
    }

    /**
     * 下拉拖动时的回调
     * @param scaleOfLayout
     */
    @Override
    public void onPull(float scaleOfLayout) {

    }

    /**
     * 释放后刷新时的回调
     */
    @Override
    public void refreshing() {
        mHeaderText.setText(mRefreshingLabel);
        if (animCircle == null) {
            circleImage.setImageResource(R.drawable.refreshing_header_anim);
            animCircle = (AnimationDrawable) circleImage.getDrawable();
        }
        animCircle.start();
    }

    /**
     * 初始化到未刷新状态
     */
    @Override
    public void reset() {
        if (animCircle != null) {
            animCircle.stop();
            animCircle = null;
        }
        circleImage.setImageResource(R.mipmap.loading_1);
    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }
}
