package com.hitwh.apptemplate.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.hitwh.apptemplate.R;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;

import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * @author SQY
 * @since 2017/3/15.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P presenter;
    protected ProgressDialog progressDialog; //
    protected Button btnReturn;
    private ProgressDialog progress; // 上传进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        presenter = createPresenter();
        super.onCreate(savedInstanceState);
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 创建presenter
     *
     * @return 处理当前View的presenter
     */
    protected abstract P createPresenter();

    @OnClick(R.id.btn_return)
    public void returnBtnClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void dismissLoading() {
        if (progressDialog == null)
            return;
        progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    public void warning(String text) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(text) // text to be displayed
                        .textColor(Color.WHITE) // change the text color
                        .color(Color.RED) // change the background color
                        .duration(1000) // one second
//                        .actionLabel("确定") // action button label
//                        .actionColor(Color.WHITE) // action button label color
                , this); // activity where it is displayed
    }

    @Override
    public void notice(String text) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(text) // text to be displayed
                        .textColor(Color.WHITE) // change the text color
                        .color(Color.BLACK) // change the background color
                        .duration(1000) // one second
//                        .actionLabel("确定") // action button label
//                        .actionColor(Color.WHITE) // action button label color
                , this); // activity where it is displayed
    }

    @Override
    public void notice(String text, ActionClickListener listener) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(text) // text to be displayed
                        .textColor(Color.WHITE) // change the text color
                        .color(Color.BLUE) // change the background color
                        .position(Snackbar.SnackbarPosition.TOP)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .actionListener(listener)
                        .actionLabel("确定") // action button label
                        .actionColor(Color.WHITE) // action button label color
                , this); // activity where it is displayed
    }

    @Override
    public void showProgressBar(int count) {
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setTitle("上传数据");
        progress.setMessage("正在上传第 1 张照片");
        progress.setMax(count);
        progress.show();
    }

    @Override
    public void updateProgress(int index, int count) {
        progress.setTitle("上传数据");
        progress.setMessage("正在上传第 " + (index + 1) + " 张照片");
        progress.setMax(count);
//        progress.setProgress((int) (((double) index) / ((double) count) * 100));
        progress.setProgress(index);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    public void dismissProgressBar() {
        progress.dismiss();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
