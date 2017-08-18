package com.apply.update.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apply.update.R;


/**
 * Created by 冯子杰(fengzijie@machine.com)
 * Date: 14-9-5
 */
public class ProgressDialogView extends RelativeLayout {

    TextView tvTitle;
    LoadingView loadingView;

    public ProgressDialogView(Context context) {
        super(context);
        initView();
    }

    public ProgressDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress_view, this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        loadingView = (LoadingView) findViewById(R.id.loading_view);
    }

    public void setText(String text) {
        tvTitle.setText(text);
    }

    public void setTextVisible(boolean visible) {
        tvTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void startAnim(){
        loadingView.startAnim();
    }

    public void stopAnim() {
        loadingView.stopAnim();
    }
}
