package com.apply.update.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apply.update.R;


/**
 * Created by 冯子杰(fengzijie@machine.com)
 * Date: 14-9-5
 */
public class LoadingView extends RelativeLayout {

    ImageView ivLoading;
    Animation rotateAnim;

    public LoadingView(Context context) {
        super(context);
        initView();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_loading, this);

        ivLoading = (ImageView) findViewById(R.id.iv_loading);

        rotateAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate360);
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnim.setInterpolator(lir);

        ivLoading.setAnimation(rotateAnim);
    }

    public void setImage(int imgResource) {
        ivLoading.setImageResource(imgResource);
    }

    public void startAnim() {
        rotateAnim.start();
    }

    public void stopAnim() {
        rotateAnim.cancel();
    }
}