package com.hitwh.apptemplate.common.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitwh.apptemplate.R;

/**
 * 上划加载底部界面
 */

public class ListFooterLayout extends FrameLayout {

    private ImageView circleImage;  // 加载图片
    private TextView tipTv;         // 加载描述
    private boolean hasMoreData = true; // 仍有数据
    private AnimationDrawable animCircle;   // 加载动画

    public ListFooterLayout(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.list_footer_loadinglayout, this);
        FrameLayout mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        circleImage = (ImageView) mInnerLayout.findViewById(R.id.iv_loading_circle);
        tipTv = (TextView) mInnerLayout.findViewById(R.id.tv_loading_tip);

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();
        lp.gravity = Gravity.TOP;
        circleImage.setImageResource(R.drawable.loading_footer_anim);
        animCircle = (AnimationDrawable) circleImage.getDrawable();

        hasMoreData = true;
        animCircle.start();
    }

    /**
     * 有数据，加载中
     */
    public void setHasData() {
        if (null == animCircle) {
            circleImage.setImageResource(R.drawable.loading_footer_anim);
            animCircle = (AnimationDrawable) circleImage.getDrawable();
        }
        tipTv.setText("正在加载");
        hasMoreData = true;
        animCircle.start();
    }

    /**
     * 已加载全部数据
     */
    public void setNoData() {
        if (null != animCircle) {
            animCircle.stop();
            animCircle = null;
        }
        hasMoreData = false;
        tipTv.setText("已加载全部");
        circleImage.setImageResource(R.mipmap.load_end);
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }

    /**
     * 加载数据失败
     */
    public void setFailed() {
        if (null != animCircle) {
            animCircle.stop();
            animCircle = null;
        }
        hasMoreData = false;
        tipTv.setText("获取数据失败");
        circleImage.setImageResource(R.mipmap.load_fail);
    }
}
