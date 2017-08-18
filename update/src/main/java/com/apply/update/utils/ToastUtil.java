package com.apply.update.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apply.update.R;


/**
 * Created by Administrator on 2017/8/14.
 */

public class ToastUtil {

    private static Toast mToast;
    private static View mLayout;

    public static void show(Context context, String message, int iconResId) {
        //实例化一个Toast对象
        if (mToast == null) {
            mLayout = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
            mToast = new Toast(context);
            mToast.setView(mLayout);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }

        //从layout中按照id查找imageView对象
        ImageView imageView = (ImageView) mLayout.findViewById(R.id.iv_toast);
        if (iconResId != -1) {
            //设置ImageView的图片
            imageView.setImageResource(iconResId);
        } else {
            imageView.setVisibility(View.GONE);
        }

        //从layout中按照id查找TextView对象,设置TextView的text内容
        if (mLayout != null) {
            ((TextView) mLayout.findViewById(R.id.tv_toast)).setText(message);
        }

        if (mToast != null) {
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }

//        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, -200);
//        toast.onShow();
    }

    public static void show(Context context, String message) {
        show(context, message, -1);
    }

    public static void cancelToast() {
        if (mToast != null)
            mToast.cancel();
    }
}
